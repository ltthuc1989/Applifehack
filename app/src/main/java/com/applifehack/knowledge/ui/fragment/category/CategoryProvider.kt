package com.applifehack.knowledge.ui.fragment.category

import com.applifehack.knowledge.ui.fragment.slidepost.SlidePostFrag
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CategoryProvider {
    @ContributesAndroidInjector
    abstract fun provideSlideFragment():SlidePostFrag


}