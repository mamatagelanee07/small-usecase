package com.andigeeky.mvpapp.ui.lines

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.andigeeky.mvpapp.api.Resource
import com.andigeeky.mvpapp.lines.repository.LinesRepository
import com.andigeeky.mvpapp.lines.vo.Line
import javax.inject.Inject

class LinesViewModel @Inject constructor(
    private val linesRepository: LinesRepository
) : ViewModel() {

    fun getLines() : LiveData<Resource<List<Line>>>{
        return linesRepository.loadLines()
    }
}
