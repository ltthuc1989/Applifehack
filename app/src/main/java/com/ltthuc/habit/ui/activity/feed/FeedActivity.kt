package com.ltthuc.habit.ui.activity.feed

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.ui.base.adapter.SingleLayoutAdapter
import com.ezyplanet.core.ui.widget.pager.RVPagerSnapHelperListenable
import com.ezyplanet.core.ui.widget.pager.RVPagerStateListener
import com.ezyplanet.core.util.extension.gotoActivity
import com.ezyplanet.core.util.extension.observe
import com.google.firebase.iid.FirebaseInstanceId
import com.ltthuc.habit.R
import com.ltthuc.habit.data.entity.Post

import com.ltthuc.habit.databinding.ActivityDailyFeedBinding
import com.ltthuc.habit.databinding.ItemFeedListBinding
import com.ltthuc.habit.ui.activity.category.CategoryActivity
import com.ltthuc.habit.ui.activity.webview.WebViewActivity
import com.ltthuc.habit.ui.widget.listener.BottomBarListener
import com.ltthuc.habit.util.CustomTabHelper
import com.ltthuc.habit.util.extension.gotoPostDetail
import kotlinx.android.synthetic.main.activity_daily_feed.*


class FeedActivity: MvvmActivity<ActivityDailyFeedBinding, FeedVM>(), FeedNav,BottomBarListener {

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


        } catch (ex:Exception){

        }

        binding.adapter = SingleLayoutAdapter<Post, ItemFeedListBinding>(
                R.layout.item_feed_list,
                emptyList(),
                viewModel
        )
        binding.listener = this
        observe(viewModel.results) {
            binding.adapter?.swapItems(it)
        }

    }


    override fun gotoFeedDetail(post: Post) {
        gotoPostDetail(post,customTabHelper)

    }

    override fun onHomeClick() {
        gotoActivity(FeedActivity::class)
    }

    override fun onCategoryClick() {
        gotoActivity(CategoryActivity::class)
    }

    override fun onSettingClick() {
    }
}