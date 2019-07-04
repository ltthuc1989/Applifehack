package com.ltthuc.habit.ui.activity

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HomeActivityVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(HomeActivityVM::class)
    abstract fun bindHomeVM(homeActivityVM: HomeActivityVM): ViewModel
}