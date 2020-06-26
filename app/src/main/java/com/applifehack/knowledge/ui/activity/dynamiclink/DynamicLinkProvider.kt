package com.applifehack.knowledge.ui.activity.dynamiclink

import com.applifehack.knowledge.ui.fragment.feed.FeedFrag
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DynamicLinkProvider {
    @ContributesAndroidInjector
    abstract fun provideFeedFrag(): FeedFrag
}