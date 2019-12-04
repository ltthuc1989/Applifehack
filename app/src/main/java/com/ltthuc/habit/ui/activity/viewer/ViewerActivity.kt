package com.ltthuc.habit.ui.activity.viewer

import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.extension.getExtra
import com.ezyplanet.core.util.extension.replaceFragment
import com.ltthuc.habit.R
import com.ltthuc.habit.databinding.ActivityViewerBinding
import com.ltthuc.habit.ui.viewer.video.VideoViewerFrag
import com.ltthuc.habit.util.AppBundleKey

class ViewerActivity : MvvmActivity<ActivityViewerBinding,ViewerVM>(),ViewerNav{

    override val viewModel: ViewerVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_viewer

    override fun onViewInitialized(binding: ActivityViewerBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this
        replaceFragment(VideoViewerFrag.newInstance(getExtra(AppBundleKey.YOUTUBE_URL)))
    }
}