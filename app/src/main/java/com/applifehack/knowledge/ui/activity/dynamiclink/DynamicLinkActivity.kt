package com.applifehack.knowledge.ui.activity.dynamiclink

import com.applifehack.knowledge.databinding.ActivityDynamicLinkBinding
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.ui.activity.BaseActivity
import com.applifehack.knowledge.ui.fragment.feed.FeedFrag
import com.applifehack.knowledge.util.AppBundleKey
import com.ezyplanet.core.util.extension.replaceFragment

class DynamicLinkActivity :BaseActivity<ActivityDynamicLinkBinding,DynamicLinkVM>(),DynamicLinkNav{

    override val viewModel: DynamicLinkVM  by getLazyViewModel()

    override val layoutId: Int = R.layout.activity_dynamic_link

    override fun onViewInitialized(binding: ActivityDynamicLinkBinding) {
        super.onViewInitialized(binding)
        viewModel.navigator = this
        binding.viewModel = viewModel
        viewModel.getPostFromId(intent?.getStringExtra(AppBundleKey.KEY_POST_ID))


    }

    override fun openFeedFragment(post: Post?) {
        setToolBar(binding.dynLinkToolbar,post?.catName!!)
        binding.tvTitle.text = post?.catName
        replaceFragment(FeedFrag().newInstance(post))
    }
}