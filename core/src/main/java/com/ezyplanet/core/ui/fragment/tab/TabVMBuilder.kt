package com.ezyplanet.core.ui.fragment.tab


import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TabVMBuilder {

    @Binds
    @IntoMap
    @ViewModelKey(TabVM::class)
    abstract fun bindMessgeListViewModel(tabVM: TabVM): ViewModel
}