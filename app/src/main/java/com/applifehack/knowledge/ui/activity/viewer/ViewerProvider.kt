package com.applifehack.knowledge.ui.activity.viewer


import com.applifehack.knowledge.ui.viewer.video.VideoViewerFrag
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ViewerProvider {
    @ContributesAndroidInjector
    abstract fun provideViewerFrag(): VideoViewerFrag


}