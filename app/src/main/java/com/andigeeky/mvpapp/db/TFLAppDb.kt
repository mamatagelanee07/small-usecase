package com.andigeeky.mvpapp.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.andigeeky.mvpapp.lines.vo.Line

/**
 * Main database description.
 */
@Database(
    entities = [Line::class],
    version = 1,
    exportSchema = false
)
abstract class TFLAppDb : RoomDatabase() {

    abstract fun linesDao(): LinesDao
}
