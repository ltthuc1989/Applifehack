package com.applifehack.knowledge.ui.activity.dynamiclink

import android.view.View
import com.applifehack.knowledge.databinding.ActivityDynamicLinkBinding
import com.applifehack.knowledge.ui.fragment.feed.FeedNav
import com.ezyplanet.core.ui.base.MvvmActivity
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.ui.activity.BaseActivity
import com.applifehack.knowledge.ui.activity.ytDetail.YtDetailActivity
import com.applifehack.knowledge.ui.widget.QuoteView
import com.applifehack.knowledge.util.AppBundleKey
import com.applifehack.knowledge.util.extension.openLink
import com.ezyplanet.core.util.extension.getExtra
import com.ezyplanet.core.util.extension.transitionActivity

class DynamicLinkActivity :BaseActivity<ActivityDynamicLinkBinding,DynamicLinkVM>(),FeedNav{

    override val viewModel: DynamicLinkVM  by getLazyViewModel()

    override val layoutId: Int = R.layout.activity_dynamic_link

    override fun onViewInitialized(binding: ActivityDynamicLinkBinding) {
        super.onViewInitialized(binding)
        viewModel.navigator = this
        binding.viewModel = viewModel
        viewModel.getPostFromId(getExtra(AppBundleKey.KEY_POST_ID))
    }

    override fun gotoFeedDetail(post: Post) {
        openBrowser(post?.redirect_link!!)


    }

    override fun gotoCatDetail(id: String?) {

    }

    override fun gotoPageUrl(post: Post) {
        openBrowser(post?.redirect_link)

    }

    override fun share(message: String) {
     shareMss(message)
    }



    override fun shareArticle(view: View) {

    }

    override fun gotoYoutubeDetail(post: Post, shareView: View) {
           transitionActivity(
            YtDetailActivity::class,
            mapOf(AppBundleKey.YOUTUBE_URL to post),shareView)

    }


}