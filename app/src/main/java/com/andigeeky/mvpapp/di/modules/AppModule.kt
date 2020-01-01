package com.andigeeky.mvpapp.di.modules

import com.andigeeky.mvpapp.BuildConfig
import com.andigeeky.mvpapp.api.ApiResponseCallAdapterFactory
import com.andigeeky.mvpapp.api.TFLService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [
    ViewModelModule::class,
    DatabaseModule::class
])
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
}
