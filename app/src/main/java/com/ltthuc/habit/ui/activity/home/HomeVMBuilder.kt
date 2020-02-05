package com.ltthuc.habit.ui.activity.home

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HomeVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(HomeVM::class)
    abstract fun bindHomeVM(homeVM: HomeVM): ViewModel
}