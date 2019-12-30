package com.andigeeky.mvpapp.di.modules

import com.andigeeky.mvpapp.ui.lines.LinesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): LinesFragment
}
