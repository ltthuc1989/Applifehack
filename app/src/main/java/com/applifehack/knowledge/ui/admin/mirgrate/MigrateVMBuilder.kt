package com.applifehack.knowledge.ui.admin.mirgrate

import androidx.lifecycle.ViewModel
import com.applifehack.knowledge.ui.admin.localpost.LocalPostVM
import com.ezyplanet.core.di.qualifier.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MigrateVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(MirgrateVM::class)
    abstract fun bindMirgrateVM(mirgrateVM: MirgrateVM): ViewModel
}