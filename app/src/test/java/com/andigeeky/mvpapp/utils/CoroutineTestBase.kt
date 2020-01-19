package com.andigeeky.mvpapp.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before

@ExperimentalCoroutinesApi

/**
 * Common Coroutine Test related configurations
 */
open class CoroutineTestBase {
    private val dispatcher = TestCoroutineDispatcher()
    val testMainContext = TestCoroutineScope(dispatcher)
    val testBackgroundContext = TestCoroutineScope()

    @Before
    fun init() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun check() {
        MatcherAssert.assertThat(testMainContext.uncaughtExceptions, CoreMatchers.`is`(emptyList()))
        MatcherAssert.assertThat(testBackgroundContext.uncaughtExceptions, CoreMatchers.`is`(emptyList()))
        Dispatchers.resetMain()
    }

    fun <T> runOnMain(block: () -> T): T {
        return runBlocking {
            val async = async(Dispatchers.Main) {
                block()
            }
            async.await()
        }
    }


    fun <T> LiveData<T>.addObserver(): CollectingObserver<T> {
        return runOnMain {
            val observer = CollectingObserver(this)
            observeForever(observer)
            observer
        }
    }

    inner class CollectingObserver<T>(
        private val liveData: LiveData<T>
    ) : Observer<T> {
        private var items = mutableListOf<T>()
        override fun onChanged(t: T) {
            items.add(t)
        }

        fun assertItems(vararg expected: T) {
            MatcherAssert.assertThat(items, CoreMatchers.`is`(expected.asList()))
        }

        fun unsubscribe() = runOnMain {
            liveData.removeObserver(this)
        }

        fun reset() = runOnMain {
            items.clear()
        }
    }
}
