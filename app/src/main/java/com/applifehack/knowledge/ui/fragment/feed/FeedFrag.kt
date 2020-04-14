package com.applifehack.knowledge.ui.fragment.feed

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProviders

import androidx.recyclerview.widget.PagerSnapHelper
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.ui.base.ViewModelScope
import com.ezyplanet.core.ui.listener.OnSnapPositionChangeListener
import com.ezyplanet.core.ui.widget.pager.SnapOnScrollListener
import com.ezyplanet.core.util.extension.attachSnapHelperWithListener
import com.ezyplanet.core.util.extension.observe
import com.ezyplanet.core.util.extension.transitionActivity

import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.entity.Post


import com.applifehack.knowledge.databinding.FragmentDailyFeedBinding
import com.applifehack.knowledge.ui.activity.BaseActivity
import com.applifehack.knowledge.ui.activity.home.HomeEventModel
import com.applifehack.knowledge.ui.activity.ytDetail.YtDetailActivity
import com.applifehack.knowledge.ui.adapter.FeedAdapter
import com.applifehack.knowledge.ui.fragment.BaseFragment
import com.applifehack.knowledge.ui.widget.QuoteView
import com.applifehack.knowledge.util.AppBundleKey
import kotlinx.android.synthetic.main.fragment_daily_feed.*


class FeedFrag : BaseFragment<FragmentDailyFeedBinding, FeedVM>(), FeedNav {

    override val viewModel: FeedVM by getLazyViewModel(ViewModelScope.FRAGMENT)
    override val layoutId: Int = R.layout.fragment_daily_feed
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

            daily_feed_recyclerview.attachSnapHelperWithListener(snapHelper, SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
                     object:OnSnapPositionChangeListener{
                override fun onSnapPositionChange(position: Int) {

                    Log.d("onSnapPosition","$position")
                    homeEventModel.toolbarTitle.value = (binding?.adapter as FeedAdapter)?.getRowData(position)?.catName

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
        fbAnalyticsHelper.logEvent(event,event,"app_sections")

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
        (activity as BaseActivity<*, *>).shareMss(message)
    }


    override fun shareImage(view: View) {
        val view = view.rootView.findViewById<QuoteView>(R.id.quoteView)
        val quote = view.getQuote()
        viewModel.generataQuote(context!!, quote)
    }

    override fun gotoYoutubeDetail(post: Post,shareView:View) {
        (activity as MvvmActivity<*, *>).transitionActivity(YtDetailActivity::class,
                mapOf(AppBundleKey.YOUTUBE_URL to post),shareView)
       // gotoActivity(YtDetailActivity::class, mapOf(AppBundleKey.YOUTUBE_URL to post.video_url))
    }



}