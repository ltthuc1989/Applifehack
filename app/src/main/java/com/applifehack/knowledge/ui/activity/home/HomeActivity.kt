package com.applifehack.knowledge.ui.activity.home

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ezyplanet.core.util.extension.gotoActivity
import com.ezyplanet.core.util.extension.replaceFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.install.model.ActivityResult
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.network.response.CatResp
import com.applifehack.knowledge.databinding.ActivityHomeBinding
import com.applifehack.knowledge.ui.activity.BaseActivity
import com.applifehack.knowledge.ui.activity.categorydetail.CategoryDetailFrag
import com.applifehack.knowledge.ui.activity.quotes.QuotesActivity
import com.applifehack.knowledge.ui.fragment.category.CategoryFrag
import com.applifehack.knowledge.ui.activity.setting.SettingActivity
import com.applifehack.knowledge.ui.activity.ytDetail.YtDetailActivity
import com.applifehack.knowledge.ui.fragment.feed.FeedFrag
import com.applifehack.knowledge.ui.widget.listener.ToolbarListener
import com.applifehack.knowledge.util.AppBundleKey
import com.applifehack.knowledge.util.helper.AppUpdateHelper

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeVM>(), HomeNav,ToolbarListener {

    override val viewModel: HomeVM by getLazyViewModel()

    override val layoutId: Int = R.layout.activity_home

    lateinit var appUpdateHelper: AppUpdateHelper
    lateinit var homeEventModel: HomeEventModel



    override fun onViewInitialized(binding: ActivityHomeBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this
        binding.listener = this
        binding.toolbarHome.setListner(this)

        replaceFragment(FeedFrag())
        initAppUpdate()
        homeEventModel = ViewModelProviders.of(this).get(HomeEventModel::class.java)
        homeEventModel?.categoryClick?.observe(this, Observer {
            binding.toolbarHome.titleBar = it.cat_name
            gotoActivity(CategoryDetailFrag::class, mapOf(CategoryFrag.KEY_CATEGORY_DETAIL to (it as CatResp)))
        })
        viewModel.showRateUse(this)
        homeEventModel?.toolbarTitle?.observe(this, Observer {
            binding.toolbarHome.titleBar = it
        })
        viewModel.handleIntent(intent)






    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppUpdateHelper.REQUEST_CODE_UPDATE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                }
                Activity.RESULT_CANCELED -> {
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {

                }
            }


        }
    }


    override fun onSetting() {
        val str = "icon_setting"
        fbAnalytics.logEvent(str,str,icon)
        gotoActivity(SettingActivity::class)
        viewModel.settingClick()

    }

    override fun onHome() {
        val str = "icon_home"
        if(viewModel.homeSelected.value!=true) {
            fbAnalytics.logEvent(str, str, icon)
            updateToolBarTitle(R.string.feed)
            replaceFragment(FeedFrag())
            viewModel.homeClick()
        }


    }

    override fun onSaved() {
    }

    override fun onCategory() {
        if(viewModel.catSelected.value!=true) {
            val str = "icon_category"
            fbAnalytics.logEvent(str, str, icon)
            updateToolBarTitle(R.string.category)
            replaceFragment(CategoryFrag())
            viewModel.catClick()
        }

    }

    private fun updateToolBarTitle(title: Int) {
        binding.toolbarHome.titleBar = getString(title)


    }



    fun initAppUpdate() {
        appUpdateHelper = AppUpdateHelper()
        appUpdateHelper?.checkUpdate(this, viewModel.getVersionCode()!!)
        lifecycle.addObserver(appUpdateHelper)
        appUpdateHelper?.observe(this, Observer {
            showSnackUpdateBar()
        })
    }

    fun showSnackUpdateBar() {
        Snackbar.make(
                findViewById(R.id.daily_feed_layout),
                "An update has just been downloaded.",
                Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("RESTART") { appUpdateHelper.completeUpdate() }
            setActionTextColor(resources.getColor(R.color.bg_app))
            show()
        }
    }

    override fun onMenu() {

    }


    override fun openArtilce(link: String?) {
       openBrowser(link)
    }

    override fun openVideo(id: String?) {
       gotoActivity(YtDetailActivity::class, mapOf(AppBundleKey.YOUTUBE_URL to id))
    }

    override fun openQuote() {
       gotoActivity(QuotesActivity::class)
    }
}