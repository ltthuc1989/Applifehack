package com.ltthuc.habit.ui.activity.category

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CategoryVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(CategoryVM::class)
    abstract fun bindCategoryVM(categoryVM: CategoryVM): ViewModel
}