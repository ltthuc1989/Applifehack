package com.applifehack.knowledge.ui.fragment.slidepost

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SlidePostVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(SlidePostVM::class)
    abstract fun bindSlidePostVM(slidePostVM: SlidePostVM): ViewModel
}