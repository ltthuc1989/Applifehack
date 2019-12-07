package com.ltthuc.habit.ui.activity.ytDetail

import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.extension.getExtra
import com.ltthuc.habit.BuildConfig
import com.ltthuc.habit.R
import com.ltthuc.habit.databinding.ActivityYoutubeDetailBinding
import com.ltthuc.habit.util.AppBundleKey
import com.ltthuc.habit.util.MediaUtil

class YtDetailActivity : MvvmActivity<ActivityYoutubeDetailBinding,YtDetailVM>(),YtDetailNav{

    override val viewModel: YtDetailVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_youtube_detail

    override fun onViewInitialized(binding: ActivityYoutubeDetailBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this
        viewModel.getYtDetail(getExtra(AppBundleKey.YOUTUBE_URL))

    }

    override fun openYoutube(url: String?) {
        MediaUtil.openYoutube(this,BuildConfig.API_YOUTUBE,url,0,true,true)
    }
}