package com.applifehack.knowledge.ui.activity.categorydetail

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CategoryDetailVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(CategoryDetailVM::class)
    abstract fun bindCategoryVM(categoryDetailVM: CategoryDetailVM): ViewModel
}