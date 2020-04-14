package com.applifehack.knowledge.ui.activity.viewer

import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.extension.getExtra
import com.ezyplanet.core.util.extension.replaceFragment
import com.applifehack.knowledge.R
import com.applifehack.knowledge.databinding.ActivityViewerBinding
import com.applifehack.knowledge.ui.viewer.video.VideoViewerFrag
import com.applifehack.knowledge.util.AppBundleKey

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