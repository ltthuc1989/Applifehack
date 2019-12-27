package com.ltthuc.habit.ui.activity.ytDetail

import androidx.core.view.GravityCompat
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.extension.getExtra
import com.ezyplanet.core.util.extension.gotoActivity
import com.ltthuc.habit.BuildConfig
import com.ltthuc.habit.R
import com.ltthuc.habit.databinding.ActivityYoutubeDetailBinding
import com.ltthuc.habit.ui.activity.category.CategoryActivity
import com.ltthuc.habit.ui.activity.feed.FeedActivity
import com.ltthuc.habit.ui.widget.listener.NavListener
import com.ltthuc.habit.ui.widget.listener.ToolbarListener
import com.ltthuc.habit.util.AppBundleKey
import com.ltthuc.habit.util.MediaUtil

class YtDetailActivity : MvvmActivity<ActivityYoutubeDetailBinding,YtDetailVM>(),YtDetailNav,NavListener,ToolbarListener{

    override val viewModel: YtDetailVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_youtube_detail

    override fun onViewInitialized(binding: ActivityYoutubeDetailBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this
        viewModel.getYtDetail(getExtra(AppBundleKey.YOUTUBE_URL))
        binding.homeNavigationView.setListner(this)
    }

    override fun openYoutube(url: String?) {
        MediaUtil.openYoutube(this,BuildConfig.API_YOUTUBE,url,0,true,true)
    }


    override fun onSetting() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onHome() {
        gotoActivity(FeedActivity::class)
    }

    override fun onSaved() {
    }

    override fun onCategory() {
        gotoActivity(CategoryActivity::class)
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