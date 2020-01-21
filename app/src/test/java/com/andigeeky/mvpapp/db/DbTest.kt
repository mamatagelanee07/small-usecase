package com.andigeeky.mvpapp.db


import android.content.Context
import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.andigeeky.mvpapp.utils.CoroutineTestBase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.TimeUnit

@UseExperimental(ExperimentalCoroutinesApi::class)
abstract class DbTest : CoroutineTestBase() {
    @Rule
    @JvmField
    val countingTaskExecutorRule = CountingTaskExecutorRule()
    lateinit var tflDatabase: TFLDatabase

    @Before
    fun initDb() {
        val app = ApplicationProvider.getApplicationContext<Context>()
        tflDatabase = Room.inMemoryDatabaseBuilder(app, TFLDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        countingTaskExecutorRule.drainTasks(10, TimeUnit.SECONDS)
        tflDatabase.close()
    }
}
