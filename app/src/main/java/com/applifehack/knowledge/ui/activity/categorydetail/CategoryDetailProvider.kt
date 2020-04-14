package com.applifehack.knowledge.ui.activity.categorydetail

import com.applifehack.knowledge.ui.fragment.articlelist.ArticleListFrag
import com.applifehack.knowledge.ui.fragment.videolist.VideoListFrag
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CategoryDetailProvider {
    @ContributesAndroidInjector
    abstract fun provideArticleListFragment():ArticleListFrag
    @ContributesAndroidInjector
    abstract fun provideVideoListFragment():VideoListFrag


}