package com.andigeeky.mvpapp.lines.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.andigeeky.mvpapp.api.vo.Resource
import com.andigeeky.mvpapp.lines.data.repository.LinesRepository
import com.andigeeky.mvpapp.lines.data.vo.Line
import com.andigeeky.mvpapp.testing.OpenForTesting
import javax.inject.Inject

@OpenForTesting
class LinesViewModel @Inject constructor(
    private val linesRepository: LinesRepository
) : ViewModel() {

    fun getLines() : LiveData<Resource<List<Line>>>{
        return linesRepository.loadLines()
    }
}
