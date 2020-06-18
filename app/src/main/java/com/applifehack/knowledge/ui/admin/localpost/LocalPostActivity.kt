package com.applifehack.knowledge.ui.admin.localpost




import android.view.View
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.databinding.ActivityLocalPostBinding

import com.applifehack.knowledge.databinding.ItemLocalPostBinding
import com.applifehack.knowledge.ui.activity.ytDetail.YtDetailActivity
import com.applifehack.knowledge.util.AppBundleKey
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.ui.base.adapter.SingleLayoutAdapter
import com.ezyplanet.core.util.extension.observe
import com.ezyplanet.core.util.extension.transitionActivity

class LocalPostActivity : MvvmActivity<ActivityLocalPostBinding,LocalPostVM>(), LocalPostNav{

    override val viewModel: LocalPostVM by getLazyViewModel()

    override val layoutId: Int = R.layout.activity_local_post

    override fun onViewInitialized(binding: ActivityLocalPostBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this



        viewModel.getPost()

        binding.adapter = SingleLayoutAdapter<Post, ItemLocalPostBinding>(
            R.layout.item_local_post,
            emptyList(),
            viewModel
        )


        observe(viewModel.results) {
            binding.adapter?.swapItems(it)
        }



    }

    override fun gotoFeedDetail(post: Post) {



    }

    override fun gotoYoutubeDetail(post: Post, shareView: View) {
         transitionActivity(
            YtDetailActivity::class,
            mapOf(AppBundleKey.YOUTUBE_URL to post),shareView)
        // gotoActivity(YtDetailActivity::class, mapOf(AppBundleKey.YOUTUBE_URL to post.video_url))
    }

    override fun scrollToTop() {
        binding.dailyFeedRecyclerview.smoothScrollToPosition(0)
    }

}