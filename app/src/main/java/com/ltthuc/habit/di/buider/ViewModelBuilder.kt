package com.ltthuc.habit.di.buider

import androidx.lifecycle.ViewModelProvider
import com.ezyplanet.core.viewmodel.ArchViewModelFactory
import dagger.Binds
import dagger.Module

/**
 * With this module all of ViewModels binds into generated Map<Class, ViewModel> and the map
 * will be injected in [ArchViewModelFactory]. In order to do this, we have to bind all
 * ViewModelBuilder modules in this module.
 *
 * And finally [ArchViewModelFactory] will be provided as [ViewModelProvider.Factory].
 *
 */
@Module(includes = [])
abstract class ViewModelBuilder {

    @Binds
    abstract fun bindViewModelFactory(archViewModelFactory: ArchViewModelFactory): ViewModelProvider.Factory
}