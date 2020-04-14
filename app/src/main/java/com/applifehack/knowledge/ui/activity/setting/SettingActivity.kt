package com.applifehack.knowledge.ui.activity.setting

import android.content.Intent
import android.net.Uri
import androidx.appcompat.widget.Toolbar
import com.ezyplanet.core.ui.base.MvvmActivity
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.databinding.ActivitySettingBinding
import com.applifehack.knowledge.util.CustomTabHelper
import com.applifehack.knowledge.util.extension.openLink
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