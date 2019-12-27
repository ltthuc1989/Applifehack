package com.ltthuc.habit.ui.activity.feed

import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.PagerSnapHelper
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.extension.gotoActivity
import com.ezyplanet.core.util.extension.observe
import com.google.firebase.iid.FirebaseInstanceId
import com.ltthuc.habit.R
import com.ltthuc.habit.data.entity.Post

import com.ltthuc.habit.databinding.ActivityDailyFeedBinding
import com.ltthuc.habit.ui.activity.category.CategoryActivity
import com.ltthuc.habit.ui.activity.categorydetail.CategoryDetailActivity
import com.ltthuc.habit.ui.adapter.FeedAdapter
import com.ltthuc.habit.ui.widget.QuoteView
import com.ltthuc.habit.ui.widget.listener.NavListener
import com.ltthuc.habit.ui.widget.listener.ToolbarListener
import com.ltthuc.habit.util.AppBundleKey
import com.ltthuc.habit.util.CustomTabHelper
import com.ltthuc.habit.util.extension.openLink
import com.ltthuc.habit.util.extension.shareMessage
import kotlinx.android.synthetic.main.activity_daily_feed.*


class FeedActivity: MvvmActivity<ActivityDailyFeedBinding, FeedVM>(),
        FeedNav, NavListener, ToolbarListener {

    override val viewModel: FeedVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_daily_feed

    private var customTabHelper: CustomTabHelper = CustomTabHelper()

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


    }

    override fun onSetting() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onHome() {
        gotoActivity(FeedActivity::class)
    }

    override fun onSaved() {
    }

    override fun onCategory() {
        gotoActivity(CategoryActivity::class)
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
        viewModel.generataQuote(this,quote)
    }
}