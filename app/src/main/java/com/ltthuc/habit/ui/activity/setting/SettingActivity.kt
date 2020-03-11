package com.ltthuc.habit.ui.activity.setting

import android.content.Intent
import android.net.Uri
import androidx.appcompat.widget.Toolbar
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ltthuc.habit.R
import com.ltthuc.habit.data.firebase.FirebaseAnalyticsHelper
import com.ltthuc.habit.databinding.ActivitySettingBinding
import com.ltthuc.habit.databinding.ActivitySplashBinding
import com.ltthuc.habit.ui.activity.BaseActivity
import com.ltthuc.habit.ui.activity.splash.SplashNav
import com.ltthuc.habit.ui.activity.splash.SplashVM
import com.ltthuc.habit.util.CustomTabHelper
import com.ltthuc.habit.util.extension.openLink
import kotlinx.android.synthetic.main.activity_setting.*
import javax.inject.Inject

class SettingActivity : MvvmActivity<ActivitySettingBinding, SettingVM>(), SettingNav {

    override val viewModel: SettingVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_setting
    @Inject lateinit var fbAnalyticsHelper: FirebaseAnalyticsHelper
    protected val customTabHelper: CustomTabHelper by lazy{
        CustomTabHelper()
    }
    override fun onViewInitialized(binding: ActivitySettingBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this
        val event = "explore_setting"
        fbAnalyticsHelper.logEvent(event,event,"app_sections")
        setToolBar(setting_toolbar)

    }

    override fun gotoPolicy(url: String) {
        openLink(url, customTabHelper)
    }

    override fun sendFeedBack(email: String) {
        val contactUsIntent = Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", email, null))
        startActivity(Intent.createChooser(contactUsIntent, getString(R.string.choose_email_app)))
    }

    override fun rateUs() {
        startActivity(Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(
                    "https://play.google.com/store/apps/details?id=com.example.android")
            setPackage("com.android.vending")
        })
    }
    private fun setToolBar(toolbar: Toolbar, title: String?="") {
        toolbar.setTitle(title)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}