package com.ltthuc.habit.ui.activity.setting

import com.ezyplanet.core.ui.base.MvvmActivity
import com.ltthuc.habit.R
import com.ltthuc.habit.databinding.ActivitySettingBinding
import com.ltthuc.habit.databinding.ActivitySplashBinding
import com.ltthuc.habit.ui.activity.BaseActivity
import com.ltthuc.habit.ui.activity.splash.SplashNav
import com.ltthuc.habit.ui.activity.splash.SplashVM
import com.ltthuc.habit.util.CustomTabHelper
import com.ltthuc.habit.util.extension.openLink

class SettingActivity : MvvmActivity<ActivitySettingBinding, SettingVM>(), SettingNav {

    override val viewModel: SettingVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_setting
    protected val customTabHelper: CustomTabHelper by lazy{
        CustomTabHelper()
    }
    override fun onViewInitialized(binding: ActivitySettingBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this

    }

    override fun gotoPolicy(url: String) {
        openLink(url, customTabHelper)
    }

}