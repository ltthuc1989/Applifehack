package com.applifehack.knowledge.ui.activity

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RSSVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(RSSVM::class)
    abstract fun bindHomeVM(RSSVM: RSSVM): ViewModel
}