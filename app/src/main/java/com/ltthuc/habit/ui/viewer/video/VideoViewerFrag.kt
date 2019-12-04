package com.ltthuc.habit.ui.viewer.video

import com.ezyplanet.core.ui.base.MvvmFragment
import com.ezyplanet.core.ui.base.ViewModelScope
import com.ezyplanet.core.util.extension.putArgs
import com.ltthuc.habit.R
import com.ltthuc.habit.databinding.FragViewerVideoBinding
import com.ltthuc.habit.ui.fragment.slidepost.SlidePostFrag
import com.ltthuc.habit.ui.fragment.videolist.VideoListNav

class VideoViewerFrag : MvvmFragment<VideoViewerVM, FragViewerVideoBinding>(),VideoViewerNav{

    companion object {
        const val KEY_VIDEO_URL= "video_url"
        val tag = this::class.java.simpleName
        fun newInstance(url:String?)= VideoViewerFrag().putArgs {
            putString(KEY_VIDEO_URL,url)

        }

    }



    override val viewModel: VideoViewerVM by getLazyViewModel(ViewModelScope.FRAGMENT)
    override val layoutId: Int = R.layout.frag_viewer_video
    override fun setUpNavigator() {
        viewModel.navigator = this
    }

    override fun onViewInitialized(binding: FragViewerVideoBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        arguments?.getString(KEY_VIDEO_URL)?.let {
            viewModel.playVideo(activity!!,it,binding.video)
        }

    }



}