package com.ltthuc.habit.ui.viewer.video

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
import com.ltthuc.habit.ui.fragment.slidepost.SlidePostVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class VideoViewerVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(VideoViewerVM::class)
    abstract fun bindVideoViewerVM(videoViewerVM: VideoViewerVM): ViewModel
}