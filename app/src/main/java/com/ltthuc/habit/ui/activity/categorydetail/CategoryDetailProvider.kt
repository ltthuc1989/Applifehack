package com.ltthuc.habit.ui.activity.categorydetail

import com.ltthuc.habit.ui.fragment.articlelist.ArticleListFrag
import com.ltthuc.habit.ui.fragment.slidepost.SlidePostFrag
import com.ltthuc.habit.ui.fragment.videolist.VideoListFrag
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CategoryDetailProvider {
    @ContributesAndroidInjector
    abstract fun provideArticleListFragment():ArticleListFrag
    @ContributesAndroidInjector
    abstract fun provideVideoListFragment():VideoListFrag


}