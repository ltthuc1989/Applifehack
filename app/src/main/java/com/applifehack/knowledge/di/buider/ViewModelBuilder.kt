package com.applifehack.knowledge.di.buider

import androidx.lifecycle.ViewModelProvider
import com.ezyplanet.core.viewmodel.ArchViewModelFactory
import com.applifehack.knowledge.ui.activity.categorydetail.CategoryDetailVMBuilder
import com.applifehack.knowledge.ui.activity.dynamiclink.DynamicLinkVMBuilder
import com.applifehack.knowledge.ui.activity.home.HomeVMBuilder
import com.applifehack.knowledge.ui.fragment.category.CategoryVMBuilder
import com.applifehack.knowledge.ui.fragment.feed.FeedVMBuilder
import com.applifehack.knowledge.ui.activity.quotes.QuotesVMBuilder
import com.applifehack.knowledge.ui.activity.setting.SettingVMBuilder
import com.applifehack.knowledge.ui.activity.splash.SplashVMBuilder
import com.applifehack.knowledge.ui.activity.viewer.ViewerVMBuilder
import com.applifehack.knowledge.ui.activity.viewer.YtDetailVMBuilder
import com.applifehack.knowledge.ui.activity.webview.WebViewVMBuilder
import com.applifehack.knowledge.ui.fragment.articlelist.ArticleListVMBuilder
import com.applifehack.knowledge.ui.fragment.favorite.FavoriteVMBuilder
import com.applifehack.knowledge.ui.fragment.slidepost.SlidePostVMBuilder
import com.applifehack.knowledge.ui.fragment.videolist.VideoListVMBuilder
import com.applifehack.knowledge.ui.viewer.video.VideoViewerVMBuilder
import dagger.Binds
import dagger.Module

/**
 * With this module all of ViewModels binds into generated Map<Class, ViewModel> and the map
 * will be injected in [ArchViewModelFactory]. In order to do this, we have to bind all
 * ViewModelBuilder modules in this module.
 *
 * And finally [ArchViewModelFactory] will be provided as [ViewModelProvider.Factory].
 *
 */
@Module(includes = [
    (SplashVMBuilder::class),
    (FeedVMBuilder::class),
    (WebViewVMBuilder::class),
    (CategoryVMBuilder::class),
    (SlidePostVMBuilder::class),
    (CategoryDetailVMBuilder::class),
    (ArticleListVMBuilder::class),
    (VideoListVMBuilder::class),
    (VideoViewerVMBuilder::class),
    (ViewerVMBuilder::class),
    (YtDetailVMBuilder::class),
    (QuotesVMBuilder::class),
    (SettingVMBuilder::class),
    (HomeVMBuilder::class),
    (DynamicLinkVMBuilder::class),
    (FavoriteVMBuilder::class)
])
abstract class ViewModelBuilder {

    @Binds
    abstract fun bindViewModelFactory(archViewModelFactory: ArchViewModelFactory): ViewModelProvider.Factory
}