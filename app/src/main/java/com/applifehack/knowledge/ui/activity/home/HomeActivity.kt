package com.applifehack.knowledge.ui.activity.home

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.network.response.CatResp
import com.applifehack.knowledge.databinding.ActivityHomeBinding
import com.applifehack.knowledge.ui.activity.BaseActivity
import com.applifehack.knowledge.ui.activity.categorydetail.CategoryDetailFrag
import com.applifehack.knowledge.ui.activity.dynamiclink.DynamicLinkActivity
import com.applifehack.knowledge.ui.activity.quotes.QuotesActivity
import com.applifehack.knowledge.ui.activity.setting.SettingActivity
import com.applifehack.knowledge.ui.activity.ytDetail.YtDetailActivity
import com.applifehack.knowledge.ui.fragment.category.CategoryFrag
import com.applifehack.knowledge.ui.fragment.favorite.FavoriteFragment
import com.applifehack.knowledge.ui.fragment.feed.FeedFrag
import com.applifehack.knowledge.ui.widget.listener.NavListener
import com.applifehack.knowledge.ui.widget.listener.ToolbarListener
import com.applifehack.knowledge.util.AppBundleKey
import com.applifehack.knowledge.util.helper.AppUpdateHelper
import com.ezyplanet.core.util.extension.gotoActivity
import com.ezyplanet.core.util.extension.replaceFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.install.model.ActivityResult


class HomeActivity : BaseActivity<ActivityHomeBinding, HomeVM>(), HomeNav,ToolbarListener,NavListener {

    override val viewModel: HomeVM by getLazyViewModel()

    override val layoutId: Int = R.layout.activity_home

    lateinit var appUpdateHelper: AppUpdateHelper
    lateinit var homeEventModel: HomeEventModel
    private var doubleBackToExitPressedOnce = false
     companion object{
         val KEY_GO_HOME="GO_HOME"
         val KEY_GO_FAVORITE ="GO_FAVORITE"
     }



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
            gotoActivity(CategoryDetailFrag::class, mapOf(CategoryFrag.KEY_CATEGORY_DETAIL to (it as CatResp)))
        })
        viewModel.showRateUse(this)
        homeEventModel?.toolbarTitle?.observe(this, Observer {
            binding.toolbarHome.titleBar = it
        })

        viewModel.subcribePush()
        viewModel.getDynamicLink(intent,this)

        homeEventModel.showRefresh.observe(this, Observer {
            binding.toolbarHome.showRefresh(it)
            binding.toolbarHome.showDate(it)
        })
        homeEventModel.datePosted.observe(this, Observer {
            binding.toolbarHome.setDatePost(it)

        })







    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if(intent?.getBooleanExtra(KEY_GO_HOME,false)==true){
            onHome()
        }else if(intent?.getBooleanExtra(KEY_GO_FAVORITE,false)==true){
            onSaved()
        }
        else{
          //  updateToolBarTitle(R.string.category)
        }
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
        if(viewModel.favoriteSeleted.value!=true) {
            val str = "icon_favorite"
            fbAnalytics.logEvent(str, str, icon)
            updateToolBarTitle(R.string.favorite)
            replaceFragment(FavoriteFragment())
            viewModel.favoriteClick()
        }
    }

    override fun onCategory() {
        if(viewModel.catSelected.value!=true) {
            val str = "icon_category"
            fbAnalytics.logEvent(str, str, icon)
           // updateToolBarTitle(R.string.category)
            binding.toolbarHome.visibility = View.GONE
            replaceFragment(CategoryFrag())
            viewModel.catClick()
        }

    }

    private fun updateToolBarTitle(title: Int) {
        binding.toolbarHome.visibility = View.VISIBLE
        binding.toolbarHome.titleBar = getString(title)


    }



    fun initAppUpdate() {
        appUpdateHelper = AppUpdateHelper()
        appUpdateHelper?.checkUpdate(this)
        lifecycle.addObserver(appUpdateHelper)
        appUpdateHelper?.observe(this, Observer {
            showSnackUpdateBar()
        })
    }

   private fun showSnackUpdateBar() {
        try {
            val snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                "An update has just been downloaded.",
                Snackbar.LENGTH_INDEFINITE
            ).apply {
                setAction("RESTART") {
                    appUpdateHelper?.completeUpdate()
                }

                setActionTextColor(Color.BLACK)
                show()
            }.view
            snackbar.setBackgroundColor(Color.WHITE)
            snackbar.findViewById<TextView>(R.id.snackbar_text).setTextColor(Color.BLACK)
        }catch (ex:Exception){

        }
    }

    override fun onMenu() {
        homeEventModel.refreshClick.postValue(true)
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

    override fun openDynamicLink(postId: String) {
        Log.e("openDynamicLink",postId)
       gotoActivity(DynamicLinkActivity::class, mapOf(AppBundleKey.KEY_POST_ID to postId))
    }



    override fun exitApp() {
        if (doubleBackToExitPressedOnce) {

            finish()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.confirm_to_exit_app), Toast.LENGTH_SHORT).show()
         android.os.Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onBackPressed() {
        exitApp()
    }
}