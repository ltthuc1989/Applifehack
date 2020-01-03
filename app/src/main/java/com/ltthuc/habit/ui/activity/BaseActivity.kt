package com.ltthuc.habit.ui.activity

import androidx.databinding.ViewDataBinding
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.extension.gotoActivity
import com.ltthuc.habit.ui.activity.category.CategoryActivity
import com.ltthuc.habit.ui.activity.feed.FeedActivity
import com.ltthuc.habit.ui.activity.setting.SettingActivity
import com.ltthuc.habit.ui.widget.listener.NavListener
import com.ltthuc.habit.util.CustomTabHelper

abstract class BaseActivity<B : ViewDataBinding, V : BaseViewModel<*, *>> : MvvmActivity<B,V>(),NavListener{

    override fun onViewInitialized(binding: B) {
        super.onViewInitialized(binding)
    }

 protected val customTabHelper: CustomTabHelper by lazy{
        CustomTabHelper()
    }

    override fun onSetting() {
        gotoActivity(SettingActivity::class)
    }

    override fun onHome() {
        gotoActivity(FeedActivity::class)
    }

    override fun onSaved() {
    }

    override fun onCategory() {
        gotoActivity(CategoryActivity::class)
    }
}