package com.applifehack.knowledge.ui.fragment.videolist

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class VideoListVMBuilder{
    @Binds
    @IntoMap
    @ViewModelKey(VideoListVM::class)
    abstract fun bindVideoListVM(videoListVM: VideoListVM): ViewModel
}