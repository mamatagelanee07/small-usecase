package com.andigeeky.mvpapp.di.modules

import android.app.Application
import androidx.room.Room
import com.andigeeky.mvpapp.db.LinesDao
import com.andigeeky.mvpapp.db.TFLDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDb(app: Application): TFLDatabase {
        return Room
            .databaseBuilder(app, TFLDatabase::class.java, "TFLApp.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideStatusDao(db: TFLDatabase): LinesDao {
        return db.linesDao()
    }
}
