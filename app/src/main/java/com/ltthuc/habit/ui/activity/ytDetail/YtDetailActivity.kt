package com.ltthuc.habit.ui.activity.ytDetail

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.ezyplanet.core.util.extension.getExtra
import com.ezyplanet.core.util.extension.getExtraParcel
import com.ltthuc.habit.BuildConfig
import com.ltthuc.habit.R
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.databinding.ActivityYoutubeDetailBinding
import com.ltthuc.habit.ui.activity.BaseActivity
import com.ltthuc.habit.ui.widget.listener.ToolbarListener
import com.ltthuc.habit.util.AppBundleKey
import com.ltthuc.habit.util.MediaUtil
import kotlinx.android.synthetic.main.activity_youtube_detail.*

class YtDetailActivity : BaseActivity<ActivityYoutubeDetailBinding,YtDetailVM>(),YtDetailNav,ToolbarListener{

    override val viewModel: YtDetailVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_youtube_detail

    override fun onViewInitialized(binding: ActivityYoutubeDetailBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        setToolBar(binding.viewToolbar, "")
        viewModel.navigator = this
        val youtubeUrl = getExtraParcel<Post>(AppBundleKey.YOUTUBE_URL)
        viewModel.getYtDetail(youtubeUrl?.video_url)
        doTransition {
            viewModel.youtubeUrl.value =youtubeUrl?.imgLink
        }

    }

    override fun openYoutube(url: String?) {
        MediaUtil.openYoutube(this,BuildConfig.API_YOUTUBE,url,0,true,true)
    }

    private fun setToolBar(toolbar: Toolbar, title: String) {
        toolbar.setTitle(title)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }




    override fun onMenu() {

    }

    override fun onSetting() {

    }

    override fun onHome() {

    }

    override fun onSaved() {

    }

    override fun onCategory() {

    }

    override fun onBackPress() {
        finishAfterTransition()
    }



}