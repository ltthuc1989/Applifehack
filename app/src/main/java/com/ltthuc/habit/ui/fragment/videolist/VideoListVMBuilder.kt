package com.ltthuc.habit.ui.fragment.videolist

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.di.qualifier.ViewModelKey
import com.ltthuc.habit.ui.fragment.slidepost.SlidePostVM
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