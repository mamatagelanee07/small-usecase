package com.andigeeky.mvpapp.di.modules

import android.app.Application
import androidx.room.Room
import com.andigeeky.mvpapp.BuildConfig
import com.andigeeky.mvpapp.api.TFLService
import com.andigeeky.mvpapp.db.LinesDao
import com.andigeeky.mvpapp.db.TFLAppDb
import com.andigeeky.mvpapp.api.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideTFLService(): TFLService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.URL_TFL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory())
            .build()
            .create(TFLService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): TFLAppDb {
        return Room
            .databaseBuilder(app, TFLAppDb::class.java, "TFLApp.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideStatusDao(db: TFLAppDb): LinesDao {
        return db.linesDao()
    }
}
