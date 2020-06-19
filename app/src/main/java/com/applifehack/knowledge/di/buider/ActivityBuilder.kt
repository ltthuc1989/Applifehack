package com.applifehack.knowledge.di.buider

import com.applifehack.knowledge.ui.activity.RSSActivity
import com.applifehack.knowledge.ui.activity.categorydetail.CategoryDetailFrag
import com.applifehack.knowledge.ui.activity.categorydetail.CategoryDetailProvider
import com.applifehack.knowledge.ui.activity.dynamiclink.DynamicLinkActivity
import com.applifehack.knowledge.ui.fragment.favorite.FavoriteFragment
import com.applifehack.knowledge.ui.activity.home.HomeActivity
import com.applifehack.knowledge.ui.activity.home.HomeProvider

import com.applifehack.knowledge.ui.activity.quotes.QuotesActivity
import com.applifehack.knowledge.ui.activity.setting.SettingActivity
import com.applifehack.knowledge.ui.activity.splash.SplashActivity
import com.applifehack.knowledge.ui.activity.viewer.ViewerActivity
import com.applifehack.knowledge.ui.activity.viewer.ViewerProvider
import com.applifehack.knowledge.ui.activity.webview.WebViewActivity
import com.applifehack.knowledge.ui.activity.ytDetail.YtDetailActivity
import com.applifehack.knowledge.ui.admin.localpost.LocalPostActivity
import com.applifehack.knowledge.ui.admin.manualpost.ManualPostActivity
import com.applifehack.knowledge.ui.admin.rssposts.RssListPostActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * The Main Module for binding all of activities.
 * Every Activity should contribute with it's related modules
 */
@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    internal abstract fun bindRssPostActivity(): RssListPostActivity
    @ContributesAndroidInjector
    internal abstract fun bindRssActivity(): RSSActivity
    @ContributesAndroidInjector
    internal abstract fun bindManualActivity(): ManualPostActivity
    @ContributesAndroidInjector
    internal abstract fun bindLocalPostActivity(): LocalPostActivity
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

    @ContributesAndroidInjector
    internal abstract fun bindDynamicLinkActivity(): DynamicLinkActivity





}
