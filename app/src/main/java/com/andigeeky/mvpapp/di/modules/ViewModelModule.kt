package com.andigeeky.mvpapp.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.andigeeky.mvpapp.di.ViewModelKey
import com.andigeeky.mvpapp.ui.lines.LinesViewModel
import com.andigeeky.mvpapp.ui.viewmodel.TFLViewModelFactory

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LinesViewModel::class)
    abstract fun bindLinesViewModel(linesViewModel: LinesViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: TFLViewModelFactory): ViewModelProvider.Factory
}
