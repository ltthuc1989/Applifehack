package com.applifehack.knowledge.ui.viewer.video

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
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