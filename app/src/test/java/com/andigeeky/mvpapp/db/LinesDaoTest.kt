package com.andigeeky.mvpapp.db

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.andigeeky.mvpapp.lines.data.vo.Line
import com.andigeeky.mvpapp.ui.util.mock
import com.andigeeky.mvpapp.ui.util.TestUtil
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.VerificationModeFactory.times
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class LinesDaoTest : DbTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @Captor
    lateinit var captor: ArgumentCaptor<ArrayList<Line>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `test insert line`() {
        runBlockingTest {
            val observer = mock<Observer<List<Line>>>()
            val line = TestUtil.createLine(0)
            tflDatabase.linesDao().insert(line)
            tflDatabase.linesDao().getLines().observeForever(observer)

            captor.run {
                verify(observer, times(1)).onChanged(capture())
                assertEquals(listOf(line), value)
            }
        }
    }

    @Test
    fun `test inserting list of line`() {
        runBlockingTest {
            val observer1 =
                mock<Observer<List<Line>>>()
            val line = TestUtil.createLine(0)
            tflDatabase.linesDao().insert(line)
            tflDatabase.linesDao().getLines().observeForever(observer1)

            captor.run {
                verify(observer1, times(1)).onChanged(capture())
                assertEquals(listOf(line), value)
            }

            val observer2 =
                mock<Observer<List<Line>>>()
            val lines = TestUtil.createLines(10)
            lines.forEach{
                tflDatabase.linesDao().insert(it)
            }
            tflDatabase.linesDao().getLines().observeForever(observer2)
            captor.run {
                verify(observer2, times(1)).onChanged(capture())
                assertEquals(lines, value)
            }
        }
    }

    @Test
    fun `test insert duplicate line is replacing data`() {
        runBlockingTest {
            val observer = mock<Observer<List<Line>>>()
            val line1 = TestUtil.createLine(0)
            val line2 = TestUtil.createLine(0)
            tflDatabase.linesDao().insert(line1)
            tflDatabase.linesDao().insert(line2)

            tflDatabase.linesDao().getLines().observeForever(observer)

            captor.run {
                verify(observer, times(1)).onChanged(capture())
                assert(value.size == 1)
            }
        }
    }
}
