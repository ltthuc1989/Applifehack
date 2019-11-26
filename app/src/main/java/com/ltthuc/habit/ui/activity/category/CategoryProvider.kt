package com.ltthuc.habit.ui.activity.category

import com.ltthuc.habit.ui.fragment.slidepost.SlidePostFrag
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CategoryProvider {
    @ContributesAndroidInjector
    abstract fun provideSlideFragment():SlidePostFrag


}