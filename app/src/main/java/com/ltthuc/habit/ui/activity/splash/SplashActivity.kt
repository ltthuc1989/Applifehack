package com.ltthuc.habit.ui.activity.splash

import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.extension.gotoActivity
import com.ltthuc.habit.R
import com.ltthuc.habit.databinding.ActivitySplashBinding
import com.ltthuc.habit.ui.activity.home.HomeActivity
import com.ltthuc.habit.ui.fragment.feed.FeedFrag

class SplashActivity : MvvmActivity<ActivitySplashBinding,SplashVM>(),SplashNav{

    override val viewModel: SplashVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_splash

    override fun onViewInitialized(binding: ActivitySplashBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this
        viewModel.updateModel("")

    }

    override fun gotoHomeScreen() {
        gotoActivity(HomeActivity::class,true)
    }
}