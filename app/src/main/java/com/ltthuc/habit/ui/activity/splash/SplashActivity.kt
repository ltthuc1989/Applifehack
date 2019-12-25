package com.ltthuc.habit.ui.activity.splash

import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.extension.gotoActivity
import com.ltthuc.habit.R
import com.ltthuc.habit.databinding.ActivitySplashBinding
import com.ltthuc.habit.ui.activity.feed.FeedActivity

class SplashActivity : MvvmActivity<ActivitySplashBinding,SplashVM>(),SplashNav{

    override val viewModel: SplashVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_splash

    override fun onViewInitialized(binding: ActivitySplashBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this
    }

    override fun gotoHomeScreen() {
        gotoActivity(FeedActivity::class)
    }
}