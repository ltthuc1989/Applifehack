package com.ltthuc.habit.ui.activity.feed

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.PagerSnapHelper
import com.ezyplanet.core.util.extension.gotoActivity
import com.ezyplanet.core.util.extension.observe
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.install.model.ActivityResult
import com.google.firebase.iid.FirebaseInstanceId
import com.ltthuc.habit.R
import com.ltthuc.habit.data.entity.Post

import com.ltthuc.habit.databinding.ActivityDailyFeedBinding
import com.ltthuc.habit.ui.activity.BaseActivity
import com.ltthuc.habit.ui.activity.category.CategoryActivity
import com.ltthuc.habit.ui.activity.categorydetail.CategoryDetailActivity
import com.ltthuc.habit.ui.activity.ytDetail.YtDetailActivity
import com.ltthuc.habit.ui.adapter.FeedAdapter
import com.ltthuc.habit.ui.widget.QuoteView
import com.ltthuc.habit.ui.widget.listener.NavListener
import com.ltthuc.habit.ui.widget.listener.ToolbarListener
import com.ltthuc.habit.util.AppBundleKey
import com.ltthuc.habit.util.extension.openLink
import com.ltthuc.habit.util.extension.shareMessage
import com.ltthuc.habit.util.helper.AppUpdateHelper
import kotlinx.android.synthetic.main.activity_daily_feed.*


class FeedActivity : BaseActivity<ActivityDailyFeedBinding, FeedVM>(), FeedNav, ToolbarListener {

    override val viewModel: FeedVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_daily_feed
    lateinit var appUpdateHelper: AppUpdateHelper


    override fun onViewInitialized(binding: ActivityDailyFeedBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) { it1 ->
            viewModel.getPost()
        }

        try {

            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(daily_feed_recyclerview)


        } catch (ex: Exception) {

        }


        binding.adapter = FeedAdapter(viewModel)

        binding.homeNavigationView.setListner(this)
        binding.dailyFeedToolbar?.setListner(this)
        observe(viewModel.results) {
            binding.adapter?.swapItems(it)
        }
        initAppUpdate()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppUpdateHelper.REQUEST_CODE_UPDATE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                }
                Activity.RESULT_CANCELED -> {
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {

                }
            }


        }
    }


    fun initAppUpdate() {
        appUpdateHelper = AppUpdateHelper()
        appUpdateHelper?.checkUpdate(this, viewModel.getVersionCode()!!)
        lifecycle.addObserver(appUpdateHelper)
        appUpdateHelper?.observe(this, Observer {
            showSnackUpdateBar()
        })
    }


    override fun gotoFeedDetail(post: Post) {
        openLink(post?.redirect_link!!, customTabHelper)


    }

    override fun gotoCatDetail(id: String?) {
        gotoActivity(CategoryDetailActivity::class, mapOf(AppBundleKey.KEY_CATEGORY_DETAIL to id))
    }

    override fun gotoPageUrl(post: Post) {
        openLink(post?.webLink, customTabHelper)

    }

    override fun share(message: String) {
        shareMessage(message)
    }

    override fun onMenu() {
        if (!isOpenDrawer || !binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            isOpenDrawer = true

        } else {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            isOpenDrawer = false
        }
    }

    override fun shareImage(view: View) {
        val view = view.rootView.findViewById<QuoteView>(R.id.quoteView)
        val quote = view.getQuote()
        viewModel.generataQuote(this, quote)
    }

    override fun gotoYoutubeDetail(post: Post) {
        gotoActivity(YtDetailActivity::class, mapOf(AppBundleKey.YOUTUBE_URL to post.video_url))
    }

    override fun showSnackUpdateBar() {
        Snackbar.make(
                findViewById(R.id.daily_feed_layout),
                "An update has just been downloaded.",
                Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("RESTART") { appUpdateHelper.completeUpdate() }
            setActionTextColor(resources.getColor(R.color.bg_app))
            show()
        }
    }
}