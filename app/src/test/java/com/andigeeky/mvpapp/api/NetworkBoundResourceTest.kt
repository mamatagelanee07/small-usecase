package com.andigeeky.mvpapp.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.andigeeky.mvpapp.utils.CoroutineTestBase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.Parameterized
import retrofit2.Response
import java.util.concurrent.atomic.AtomicReference

@UseExperimental(ObsoleteCoroutinesApi::class)
@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class NetworkBoundResourceTest : CoroutineTestBase() {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dbData = MutableLiveData<Foo>()

    @Test
    fun `test data from network saved properly in db`() {
        val saved = AtomicReference<Foo>()
        val liveData = networkBoundResource(
            saveCallResult = {
                withContext(testBackgroundContext.coroutineContext) {
                    saved.set(it)
                    dbData.postValue(it)
                }
            },
            fetch = { ApiResponse.create(Response.success(Foo(1))) },
            loadFromDb = { dbData }
        )
        val collection = liveData.addObserver()
        collection.assertItems(
            Resource.loading(null)
        )
        dbData.value = null
        assertThat(saved.get(), `is`(Foo(1)))
        collection.assertItems(
            Resource.loading(null),
            Resource.success(Foo(1))
        )
    }

    @Test
    fun `test error from network shall not saved in db`() {
        val body = ResponseBody.create(MediaType.parse("text/html"), "error")

        val liveData = networkBoundResource(
            saveCallResult = { error("shouldn't save") },
            fetch = { ApiResponse.create(Response.error<Foo>(500, body)) },
            loadFromDb = { dbData }
        )
        dbData.value = null
        liveData.addObserver().apply {
            assertItems(
                Resource.loading(null),
                Resource.error("error", null)
            )
        }
    }

    @Test
    fun `test in-case of shouldFetch false data is fetched only from db`() {
        val liveData = networkBoundResource<Foo, Foo>(
            saveCallResult = { error("nothing to save, it is from db") },
            shouldFetch = { false },
            fetch = { error("no reason to fetch") },
            loadFromDb = { dbData }
        )
        liveData.addObserver().apply {
            assertItems(Resource.loading(null))
            reset()
            dbData.value = Foo(1)
            assertItems(Resource.success(Foo(1)))
            reset()
            dbData.value = Foo(2)
            assertItems(Resource.success(Foo(2)))
        }
    }

    @Test
    fun `test for network failure error contains data from db`() {
        val executeNetwork = CompletableDeferred<Unit>()
        val ld = networkBoundResource(
            saveCallResult = { error("should not try to save") },
            fetch = {
                executeNetwork.await()
                val body = ResponseBody.create(MediaType.parse("text/html"), "error")
                ApiResponse.create(Response.error<Foo>(400, body))
            },
            loadFromDb = { dbData },
            shouldFetch = { true }
        )
        ld.addObserver().apply {
            assertItems(Resource.loading(null))
            reset()
            dbData.value = Foo(1)
            assertItems(Resource.loading(Foo(1)))
            reset()
            executeNetwork.complete(Unit)
            assertItems(Resource.error("error", Foo(1)))
            reset()
            dbData.value = Foo(2)
            assertItems(Resource.error("error", Foo(2)))
        }
    }

    @Test
    fun `test data returned is latest from network if db and network both succeed`() {
        val executeNetwork = CompletableDeferred<Unit>()
        val saved = AtomicReference<Foo>()
        val ld = networkBoundResource(
            saveCallResult = {
                assertThat(saved.compareAndSet(null, it), `is`(true))
                dbData.value = it
            },
            fetch = {
                executeNetwork.await()
                ApiResponse.create(Response.success(Foo(2)))
            },
            loadFromDb = { dbData },
            shouldFetch = { true }
        )
        ld.addObserver().apply {
            assertItems(Resource.loading(null))
            reset()
            dbData.value = Foo(1)
            assertItems(Resource.loading(Foo(1)))
            reset()
            executeNetwork.complete(Unit)
            assertItems(Resource.success(Foo(2)))
            assertThat(saved.get(), `is`(Foo(2)))
        }
    }

    @Test
    fun `test after losing subscription no further calls to db and network is made`() {
        val dbData = MutableLiveData<Foo>()
        val ld = networkBoundResource<Foo, Foo>(
            saveCallResult = {throw AssertionError("should not call")},
            fetch = {throw AssertionError("should not call")},
            loadFromDb = { dbData }
        )
        ld.addObserver().apply {
            assertItems(Resource.loading(null))
            assertThat(dbData.hasObservers(), `is`(true))
            unsubscribe()
            advanceTimeBy(10_000)
            assertThat(dbData.hasObservers(), `is`(false))
            assertItems(Resource.loading(null))
        }
    }

    private fun advanceTimeBy(time: Long) {
        testMainContext.advanceTimeBy(time)
        testBackgroundContext.advanceTimeBy(time)
    }

    private data class Foo(var value: Int)

    companion object {
        @Parameterized.Parameters
        @JvmStatic
        fun param(): List<Boolean> {
            return arrayListOf(true, false)
        }
    }
}
