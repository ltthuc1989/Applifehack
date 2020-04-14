package com.applifehack.knowledge.ui.activity.home

import com.applifehack.knowledge.ui.fragment.articlelist.ArticleListFrag
import com.applifehack.knowledge.ui.fragment.category.CategoryFrag
import com.applifehack.knowledge.ui.fragment.feed.FeedFrag
import com.applifehack.knowledge.ui.fragment.slidepost.SlidePostFrag
import com.applifehack.knowledge.ui.fragment.videolist.VideoListFrag
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