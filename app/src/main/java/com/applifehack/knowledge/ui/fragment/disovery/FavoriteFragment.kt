package com.applifehack.knowledge.ui.fragment.disovery

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.PagerSnapHelper
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.databinding.FragmentDailyFeedBinding
import com.applifehack.knowledge.ui.activity.BaseActivity
import com.applifehack.knowledge.ui.activity.home.HomeEventModel
import com.applifehack.knowledge.ui.activity.ytDetail.YtDetailActivity
import com.applifehack.knowledge.ui.adapter.FeedAdapter
import com.applifehack.knowledge.ui.fragment.BaseFragment
import com.applifehack.knowledge.ui.fragment.feed.FeedNav
import com.applifehack.knowledge.ui.fragment.feed.FeedVM
import com.applifehack.knowledge.util.AppBundleKey
import com.applifehack.knowledge.util.extension.openLink
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.ui.base.MvvmFragment
import com.ezyplanet.core.ui.base.ViewModelScope
import com.ezyplanet.core.ui.listener.OnSnapPositionChangeListener
import com.ezyplanet.core.ui.widget.pager.SnapOnScrollListener
import com.ezyplanet.core.util.extension.attachSnapHelperWithListener
import com.ezyplanet.core.util.extension.observe
import com.ezyplanet.core.util.extension.transitionActivity
import kotlinx.android.synthetic.main.activity_quote.*

class FavoriteFragment : BaseFragment<FragmentDailyFeedBinding,FavoriteVM>(),FeedNav {

    override val viewModel: FavoriteVM by getLazyViewModel(ViewModelScope.FRAGMENT)
    override val layoutId: Int = com.applifehack.knowledge.R.layout.fragment_daily_feed
    private var isFirstCreated = false
    lateinit var homeEventModel: HomeEventModel


    override fun setUpNavigator() {
        viewModel.navigator = this
    }

    override fun onViewInitialized(binding: FragmentDailyFeedBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this

        homeEventModel = ViewModelProviders.of(activity!!).get(HomeEventModel::class.java)

        viewModel.getPost()


        try {

            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(daily_feed_recyclerview)

            daily_feed_recyclerview.attachSnapHelperWithListener(snapHelper,
                SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
                object : OnSnapPositionChangeListener {
                    override fun onSnapPositionChange(position: Int) {

                        Log.d("onSnapPosition", "$position")

                        viewModel.onLoadMore(position)

                    }
                })

        } catch (ex: Exception) {

        }


        binding.adapter = FeedAdapter(viewModel)

        observe(viewModel.results) {
            binding.adapter?.swapItems(it)
        }
        val event = "explore_feed"
        homeEventModel.showRefresh.value = false

    }


    override fun gotoFeedDetail(post: Post) {
        openLink(post?.redirect_link!!)


    }

    override fun gotoCatDetail(id: String?) {
        // goto(CategoryDetailFrag::class, mapOf(AppBundleKey.KEY_CATEGORY_DETAIL to id))
    }

    override fun gotoPageUrl(post: Post) {
        openLink(post?.redirect_link)

    }

    override fun share(message: String) {

    }


    override fun shareArticle(view: View) {

    }

    override fun gotoYoutubeDetail(post: Post, shareView: View) {
        (activity as MvvmActivity<*, *>).transitionActivity(
            YtDetailActivity::class,
            mapOf(AppBundleKey.YOUTUBE_URL to post), shareView
        )

    }

    override fun scrollToTop() {
        TODO("Not yet implemented")
    }
}



