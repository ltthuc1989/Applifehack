package com.applifehack.knowledge.ui.activity.splash

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SplashVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(SplashVM::class)
    abstract fun bindSplashVM(splashVM: SplashVM): ViewModel
}