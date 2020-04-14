package com.applifehack.knowledge.ui.fragment.articlelist

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
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