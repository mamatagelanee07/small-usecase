package com.andigeeky.mvpapp.di

import android.app.Application
import com.andigeeky.mvpapp.TFLApplication
import com.andigeeky.mvpapp.di.modules.AppModule
import com.andigeeky.mvpapp.di.modules.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidInjectionModule::class,
        MainActivityModule::class,
        AppModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(TFLApplication: TFLApplication)
}
