package com.applifehack.knowledge.ui.admin.rssposts

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RssListPostVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(RssListPostVM::class)
    abstract fun bindListPostVM(rssListPostVM: RssListPostVM): ViewModel
}