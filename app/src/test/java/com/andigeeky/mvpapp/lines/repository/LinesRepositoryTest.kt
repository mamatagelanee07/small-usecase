package com.andigeeky.mvpapp.lines.repository

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.andigeeky.mvpapp.api.ApiResponse
import com.andigeeky.mvpapp.api.FakeTFLService
import com.andigeeky.mvpapp.api.Resource
import com.andigeeky.mvpapp.db.LinesDao
import com.andigeeky.mvpapp.db.TFLDatabase
import com.andigeeky.mvpapp.utils.CoroutineTestBase
import com.andigeeky.mvpapp.utils.resourcepools.TestUtil
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Response

@UseExperimental(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class LinesRepositoryTest : CoroutineTestBase(){
    private lateinit var linesRepository: LinesRepository
    private lateinit var linesDao : LinesDao
    private val tflService = FakeTFLService()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        val app = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(app, TFLDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        linesDao = db.linesDao()
        linesRepository = LinesRepository(
            linesDao,
            tflService
        )
    }

    @Test
    fun `test load lines from db`(){
        val lines = TestUtil.createLines(10)
        val calledService = CompletableDeferred<Unit>()
        runBlocking {
            tflService.linesImpl = {
                calledService.complete(Unit)
                ApiResponse.create(Response.success(lines))
            }

            linesRepository.loadLines().addObserver().apply {
                calledService.await()
                assertItems(
                    Resource.loading(null),
                    Resource.loading(emptyList()),
                    Resource.success(lines)
                )
            }

            linesDao.getLines().addObserver().apply {
                assertItems(lines)
            }
        }
    }
}
