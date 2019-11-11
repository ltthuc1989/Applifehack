package com.ltthuc.habit.ui.activity.listpost

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ListPostVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(ListPostVM::class)
    abstract fun bindListPostVM(listPostVM: ListPostVM): ViewModel
}