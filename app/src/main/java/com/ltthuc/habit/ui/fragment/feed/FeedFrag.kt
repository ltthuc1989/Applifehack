package com.ltthuc.habit.ui.fragment.feed

import android.app.Activity
import android.content.Intent
import android.view.View

import androidx.recyclerview.widget.PagerSnapHelper
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.ui.base.ViewModelScope
import com.ezyplanet.core.util.extension.observe
import com.ezyplanet.core.util.extension.transitionActivity

import com.ltthuc.habit.R
import com.ltthuc.habit.data.entity.Post


import com.ltthuc.habit.databinding.FragmentDailyFeedBinding
import com.ltthuc.habit.ui.activity.BaseActivity
import com.ltthuc.habit.ui.activity.ytDetail.YtDetailActivity
import com.ltthuc.habit.ui.adapter.FeedAdapter
import com.ltthuc.habit.ui.fragment.BaseFragment
import com.ltthuc.habit.ui.widget.QuoteView
import com.ltthuc.habit.ui.widget.listener.ToolbarListener
import com.ltthuc.habit.util.AppBundleKey
import com.ltthuc.habit.util.helper.AppUpdateHelper
import kotlinx.android.synthetic.main.fragment_daily_feed.*


class FeedFrag : BaseFragment<FragmentDailyFeedBinding, FeedVM>(), FeedNav {

    override val viewModel: FeedVM by getLazyViewModel(ViewModelScope.FRAGMENT)
    override val layoutId: Int = R.layout.fragment_daily_feed


    override fun setUpNavigator() {
        viewModel.navigator = this
    }

    override fun onViewInitialized(binding: FragmentDailyFeedBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this



        viewModel.getPost()

        try {

            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(daily_feed_recyclerview)


        } catch (ex: Exception) {

        }


        binding.adapter = FeedAdapter(viewModel)

        observe(viewModel.results) {
            binding.adapter?.swapItems(it)
        }


    }


    override fun gotoFeedDetail(post: Post) {
        openLink(post?.url!!)


    }

    override fun gotoCatDetail(id: String?) {
        // goto(CategoryDetailFrag::class, mapOf(AppBundleKey.KEY_CATEGORY_DETAIL to id))
    }

    override fun gotoPageUrl(post: Post) {
        openLink(post?.webLink)

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