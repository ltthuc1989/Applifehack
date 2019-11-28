package com.ltthuc.habit.ui.fragment.articlelist

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
import com.ltthuc.habit.ui.fragment.slidepost.SlidePostVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ArticleListVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(ArticleListVM::class)
    abstract fun bindArticlePostVM(articleListVM: ArticleListVM): ViewModel
}