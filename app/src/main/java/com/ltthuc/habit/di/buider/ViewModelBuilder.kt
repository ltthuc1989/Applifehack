package com.ltthuc.habit.di.buider

import androidx.lifecycle.ViewModelProvider
import com.ezyplanet.core.viewmodel.ArchViewModelFactory
import com.ltthuc.habit.ui.activity.HomeActivityVM
import com.ltthuc.habit.ui.activity.HomeActivityVMBuilder
import com.ltthuc.habit.ui.activity.category.CategoryVMBuilder
import com.ltthuc.habit.ui.activity.categorydetail.CategoryDetailVMBuilder
import com.ltthuc.habit.ui.activity.feed.FeedVMBuilder
import com.ltthuc.habit.ui.activity.listpost.ListPostVMBuilder
import com.ltthuc.habit.ui.activity.webview.WebViewVMBuilder
import com.ltthuc.habit.ui.fragment.articlelist.ArticleListVMBuilder
import com.ltthuc.habit.ui.fragment.slidepost.SlidePostVMBuilder
import com.ltthuc.habit.ui.fragment.videolist.VideoListVMBuilder
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
    (HomeActivityVMBuilder::class),
    (ListPostVMBuilder::class),
    (FeedVMBuilder::class),
    (WebViewVMBuilder::class),
    (CategoryVMBuilder::class),
    (SlidePostVMBuilder::class),
    (CategoryDetailVMBuilder::class),
    (ArticleListVMBuilder::class),
    (VideoListVMBuilder::class)
])
abstract class ViewModelBuilder {

    @Binds
    abstract fun bindViewModelFactory(archViewModelFactory: ArchViewModelFactory): ViewModelProvider.Factory
}