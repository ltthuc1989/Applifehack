package com.ltthuc.habit.ui.activity.viewer


import com.ltthuc.habit.ui.viewer.video.VideoViewerFrag
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ViewerProvider {
    @ContributesAndroidInjector
    abstract fun provideViewerFrag(): VideoViewerFrag


}