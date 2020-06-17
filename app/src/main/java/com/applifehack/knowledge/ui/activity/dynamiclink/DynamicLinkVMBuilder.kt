package com.applifehack.knowledge.ui.activity.dynamiclink

import androidx.lifecycle.ViewModel
import com.applifehack.knowledge.ui.activity.home.HomeVM
import com.ezyplanet.core.di.qualifier.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DynamicLinkVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(DynamicLinkVM::class)
    abstract fun bindDynamicLinkVM(dynamicLinkVM: DynamicLinkVM): ViewModel
}