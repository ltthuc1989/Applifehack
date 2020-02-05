package com.ltthuc.habit.ui.activity.home

import com.ltthuc.habit.ui.fragment.articlelist.ArticleListFrag
import com.ltthuc.habit.ui.fragment.category.CategoryFrag
import com.ltthuc.habit.ui.fragment.feed.FeedFrag
import com.ltthuc.habit.ui.fragment.slidepost.SlidePostFrag
import com.ltthuc.habit.ui.fragment.videolist.VideoListFrag
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeProvider {
    @ContributesAndroidInjector
    abstract fun provideFeedFrag(): FeedFrag

    @ContributesAndroidInjector
    abstract fun provideCategoryFrag(): CategoryFrag

    @ContributesAndroidInjector
    abstract fun provideArticleListFragment(): ArticleListFrag

    @ContributesAndroidInjector
    abstract fun provideVideoListFragment(): VideoListFrag

    @ContributesAndroidInjector
    abstract fun provideSlideFragment(): SlidePostFrag






}