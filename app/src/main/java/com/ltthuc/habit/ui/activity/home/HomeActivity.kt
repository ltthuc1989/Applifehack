package com.ltthuc.habit.ui.activity.home

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ezyplanet.core.util.extension.gotoActivity
import com.ezyplanet.core.util.extension.replaceFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.install.model.ActivityResult
import com.ltthuc.habit.R
import com.ltthuc.habit.data.network.response.CatResp
import com.ltthuc.habit.databinding.ActivityHomeBinding
import com.ltthuc.habit.ui.activity.BaseActivity
import com.ltthuc.habit.ui.activity.categorydetail.CategoryDetailFrag
import com.ltthuc.habit.ui.fragment.category.CategoryFrag
import com.ltthuc.habit.ui.activity.setting.SettingActivity
import com.ltthuc.habit.ui.fragment.feed.FeedFrag
import com.ltthuc.habit.ui.widget.listener.NavListener
import com.ltthuc.habit.ui.widget.listener.ToolbarListener
import com.ltthuc.habit.util.helper.AppUpdateHelper

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeVM>(), HomeNav,ToolbarListener {

    override val viewModel: HomeVM by getLazyViewModel()

    override val layoutId: Int = R.layout.activity_home

    lateinit var appUpdateHelper: AppUpdateHelper
    lateinit var homeEventModel: HomeEventModel

    override fun onViewInitialized(binding: ActivityHomeBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this
        binding.homeNavigationView.setListner(this)
        binding.toolbarHome.setListner(this)

        replaceFragment(FeedFrag())
        initAppUpdate()
        homeEventModel = ViewModelProviders.of(this).get(HomeEventModel::class.java)
        homeEventModel?.categoryClick?.observe(this, Observer {
            binding.toolbarHome.titleBar = it.name
            gotoActivity(CategoryDetailFrag::class, mapOf(CategoryFrag.KEY_CATEGORY_DETAIL to (it as CatResp)))
        })
        viewModel.showRateUse(this)





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
        onMenu()
        gotoActivity(SettingActivity::class)
    }

    override fun onHome() {

        updateToolBarTitle(R.string.feed)
        replaceFragment(FeedFrag())

    }

    override fun onSaved() {
    }

    override fun onCategory() {

        updateToolBarTitle(R.string.category)
        replaceFragment(CategoryFrag())

    }

    private fun updateToolBarTitle(title: Int) {
        binding.toolbarHome.titleBar = getString(title)
        onMenu()

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
        if (!isOpenDrawer || !binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            isOpenDrawer = true

        } else {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            isOpenDrawer = false
        }
    }



}