package com.applifehack.knowledge.ui.activity.viewer

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
import com.applifehack.knowledge.ui.activity.ytDetail.YtDetailVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class YtDetailVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(YtDetailVM::class)
    abstract fun bindViewerVM(ytDetailVM: YtDetailVM): ViewModel
}