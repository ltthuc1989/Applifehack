package com.applifehack.knowledge.ui.admin.manualpost

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ManualPostVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(ManualPostVM::class)
    abstract fun bindHomeVM(manualPostVM: ManualPostVM): ViewModel
}