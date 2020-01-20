package com.andigeeky.mvpapp.lines.repository

import androidx.lifecycle.LiveData
import com.andigeeky.mvpapp.api.Resource
import com.andigeeky.mvpapp.api.TFLService
import com.andigeeky.mvpapp.api.networkBoundResource
import com.andigeeky.mvpapp.db.LinesDao
import com.andigeeky.mvpapp.lines.vo.Line
import com.andigeeky.mvpapp.testing.OpenForTesting
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles Line instances.
 *
 */
@OpenForTesting
@Singleton
class LinesRepository @Inject constructor(
    private val linesDao: LinesDao,
    private val tflService: TFLService
) {
    /**
     * Returns all lines from [TFLService]
     */
    fun loadLines(): LiveData<Resource<List<Line>>> {
        return networkBoundResource(
            saveCallResult = {
                it.forEach { line ->
                    linesDao.insert(line)
                }
            },
            fetch = { tflService.getLines() },
            loadFromDb = { linesDao.getLines() }
        )
    }
}
