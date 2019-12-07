package com.ltthuc.habit.ui.activity.viewer

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
import com.ltthuc.habit.ui.activity.ytDetail.YtDetailVM
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