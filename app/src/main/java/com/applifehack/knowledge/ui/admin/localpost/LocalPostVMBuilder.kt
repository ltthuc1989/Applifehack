package com.applifehack.knowledge.ui.admin.localpost

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class LocalPostVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(LocalPostVM::class)
    abstract fun bindLocalPostVM(localPostVM: LocalPostVM): ViewModel
}