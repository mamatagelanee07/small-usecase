package com.andigeeky.mvpapp.lines.repository

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.andigeeky.mvpapp.api.ApiResponse
import com.andigeeky.mvpapp.api.Resource
import com.andigeeky.mvpapp.api.TFLService
import com.andigeeky.mvpapp.db.LinesDao
import com.andigeeky.mvpapp.db.TFLDatabase
import com.andigeeky.mvpapp.utils.CoroutineTestBase
import com.andigeeky.mvpapp.util.mock
import com.andigeeky.mvpapp.util.TestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Response

@UseExperimental(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class LinesRepositoryTest : CoroutineTestBase(){
    private lateinit var linesRepository: LinesRepository
    private lateinit var linesDao : LinesDao
    private val tflService = mock<TFLService>()

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
        runBlockingTest {
            Mockito.`when`(tflService.getLines()).thenReturn(
                ApiResponse.create(Response.success(lines)))

            linesRepository.loadLines().addObserver().apply {
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
