package com.andigeeky.mvpapp

import android.app.Application
import com.andigeeky.mvpapp.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class TFLApplication : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        AppInjector.init(this)
    }

    override fun androidInjector()= dispatchingAndroidInjector
}
