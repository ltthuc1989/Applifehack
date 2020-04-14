package com.applifehack.knowledge.ui.activity.viewer

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewerVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(ViewerVM::class)
    abstract fun bindViewerVM(viewerVM: ViewerVM): ViewModel
}