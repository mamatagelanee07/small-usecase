package com.andigeeky.mvpapp.ui.lines

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.andigeeky.mvpapp.api.Resource
import com.andigeeky.mvpapp.lines.repository.LinesRepository
import com.andigeeky.mvpapp.lines.vo.Line
import com.andigeeky.mvpapp.utils.mock
import com.andigeeky.mvpapp.utils.resourcepools.TestUtil
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class LinesViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val linesRepository = mock(LinesRepository::class.java)
    private val linesViewModel = LinesViewModel(linesRepository)

    @Test
    fun `test lines live data call loadLines()`() {
        val foo = MutableLiveData<Resource<List<Line>>>()
        `when`(linesRepository.loadLines()).thenReturn(foo)

        linesViewModel.getLines().observeForever(mock())
        verify(linesRepository).loadLines()
        reset(linesRepository)
    }

    @Test
    fun `test sending data to UI`() {
        val foo = MutableLiveData<Resource<List<Line>>>()
        `when`(linesRepository.loadLines()).thenReturn(foo)
        val observer = mock<Observer<Resource<List<Line>>>>()

        linesViewModel.getLines().observeForever(observer)
        verify(observer, never()).onChanged(any())

        val fooUser = TestUtil.createLines(10)
        val fooValue = Resource.success(fooUser)
        foo.value = fooValue
        verify(observer).onChanged(fooValue)
        reset(observer)
    }
}
