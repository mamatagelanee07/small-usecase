package com.andigeeky.mvpapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andigeeky.mvpapp.lines.vo.Line

/**
 * Interface for database access for User related operations.
 */
@Dao
interface LinesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(line: Line)

    @Query("SELECT * FROM Lines")
    fun getLines(): LiveData<List<Line>>
}