package com.andigeeky.mvpapp.lines.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andigeeky.mvpapp.lines.data.vo.Line
import com.andigeeky.mvpapp.testing.OpenForTesting

/**
 * Interface for database access for User related operations.
 */
@Dao
@OpenForTesting
interface LinesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(line: Line)

    @Query("SELECT * FROM Lines")
    fun getLines(): LiveData<List<Line>>
}
