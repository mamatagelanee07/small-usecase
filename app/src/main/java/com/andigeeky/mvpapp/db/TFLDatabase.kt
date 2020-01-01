package com.andigeeky.mvpapp.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.andigeeky.mvpapp.lines.vo.Line

/**
 * TFL database for Lines
 */
@Database(
    entities = [Line::class],
    version = 1,
    exportSchema = false
)
abstract class TFLDatabase : RoomDatabase() {

    abstract fun linesDao(): LinesDao
}
