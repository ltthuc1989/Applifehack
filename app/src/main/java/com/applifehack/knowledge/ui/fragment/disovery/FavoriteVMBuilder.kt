package com.applifehack.knowledge.ui.fragment.disovery

import androidx.lifecycle.ViewModel
import com.applifehack.knowledge.ui.fragment.feed.FeedVM
import com.ezyplanet.core.di.qualifier.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FavoriteVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(FavoriteVM::class)
    abstract fun bindFavoriteVM(favoriteVM: FavoriteVM): ViewModel
}