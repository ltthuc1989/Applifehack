package com.applifehack.knowledge.ui.fragment.feed

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FeedVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(FeedVM::class)
    abstract fun bindFeedVM(feedVM: FeedVM): ViewModel
}