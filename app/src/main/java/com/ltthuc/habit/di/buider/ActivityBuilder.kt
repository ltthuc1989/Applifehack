package com.ltthuc.habit.di.buider

import com.ltthuc.habit.ui.activity.categorydetail.CategoryDetailFrag
import com.ltthuc.habit.ui.activity.categorydetail.CategoryDetailProvider
import com.ltthuc.habit.ui.activity.home.HomeActivity
import com.ltthuc.habit.ui.activity.home.HomeProvider

import com.ltthuc.habit.ui.activity.quotes.QuotesActivity
import com.ltthuc.habit.ui.activity.setting.SettingActivity
import com.ltthuc.habit.ui.activity.splash.SplashActivity
import com.ltthuc.habit.ui.activity.viewer.ViewerActivity
import com.ltthuc.habit.ui.activity.viewer.ViewerProvider
import com.ltthuc.habit.ui.activity.webview.WebViewActivity
import com.ltthuc.habit.ui.activity.ytDetail.YtDetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * The Main Module for binding all of activities.
 * Every Activity should contribute with it's related modules
 */
@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    internal abstract fun bindWebViewActivity(): WebViewActivity

    @ContributesAndroidInjector(modules = [(HomeProvider::class)])
    internal abstract fun bindHomeActivity(): HomeActivity

    @ContributesAndroidInjector(modules = [(ViewerProvider::class)])
    internal abstract fun bindViewerActivity(): ViewerActivity
    @ContributesAndroidInjector
    internal abstract fun bindYtDetailActivity(): YtDetailActivity
    @ContributesAndroidInjector
    internal abstract fun bindQuoteActivity(): QuotesActivity
    @ContributesAndroidInjector
    internal abstract fun bindSettingActivity(): SettingActivity
    @ContributesAndroidInjector
    internal abstract fun bindSplashActivity(): SplashActivity
    @ContributesAndroidInjector(modules = [(CategoryDetailProvider::class)])
    internal abstract fun bindCategoryDetailActivity(): CategoryDetailFrag

}
