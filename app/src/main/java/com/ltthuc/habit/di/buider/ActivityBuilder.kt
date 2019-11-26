package com.ltthuc.habit.di.buider

import com.ltthuc.habit.ui.activity.HomeActivity
import com.ltthuc.habit.ui.activity.category.CategoryActivity
import com.ltthuc.habit.ui.activity.category.CategoryProvider
import com.ltthuc.habit.ui.activity.feed.FeedActivity
import com.ltthuc.habit.ui.activity.listpost.ListPostActivity
import com.ltthuc.habit.ui.activity.webview.WebViewActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * The Main Module for binding all of activities.
 * Every Activity should contribute with it's related modules
 */
@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector
    internal abstract fun bindHomeActivity(): HomeActivity
    @ContributesAndroidInjector
    internal abstract fun bindListPostActivity(): ListPostActivity
    @ContributesAndroidInjector
    internal abstract fun bindFeedActivity(): FeedActivity
    @ContributesAndroidInjector
    internal abstract fun bindWebViewActivity(): WebViewActivity
    @ContributesAndroidInjector(modules = [(CategoryProvider::class)])
    internal abstract fun bindCategoryActivity(): CategoryActivity

}
