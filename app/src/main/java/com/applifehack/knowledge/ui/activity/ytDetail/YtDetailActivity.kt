package com.applifehack.knowledge.ui.activity.ytDetail

import com.applifehack.knowledge.BuildConfig
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.databinding.ActivityYoutubeDetailBinding
import com.applifehack.knowledge.ui.activity.BaseActivity
import com.applifehack.knowledge.ui.widget.listener.ToolbarListener
import com.applifehack.knowledge.util.AppBundleKey
import com.applifehack.knowledge.util.MediaUtil

class YtDetailActivity : BaseActivity<ActivityYoutubeDetailBinding,YtDetailVM>(),YtDetailNav,ToolbarListener{

    override val viewModel: YtDetailVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_youtube_detail

    override fun onViewInitialized(binding: ActivityYoutubeDetailBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        setToolBar(binding.viewToolbar, "")
        viewModel.navigator = this
        val post = intent.getParcelableExtra<Post>(AppBundleKey.YOUTUBE_URL)
        viewModel.getYtDetail(post!!.video_url)
        val event = "explore_article"
       // fbAnalytics.logEvent(event,event,"app_sections")


    }

    override fun openYoutube(url: String?) {
        MediaUtil.openYoutube(this,BuildConfig.API_YOUTUBE,url,0,true,true)
    }



    override fun onMenu() {

    }



    override fun onBackPress() {
        finishAfterTransition()
    }



}