package com.ltthuc.habit.di.buider

import com.ltthuc.habit.ui.activity.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * The Main Module for binding all of activities.
 * Every Activity should contribute with it's related modules
 */
@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector
    internal abstract fun bindHomeActivity(): HomeActivity

}
