package com.applifehack.knowledge.ui.activity.quotes

import androidx.recyclerview.widget.PagerSnapHelper
import com.ezyplanet.core.util.extension.gotoActivity
import com.ezyplanet.core.util.extension.observe
import com.applifehack.knowledge.R
import com.applifehack.knowledge.databinding.ActivityQuoteBinding
import com.applifehack.knowledge.ui.activity.BaseActivity
import com.applifehack.knowledge.ui.fragment.favorite.FavoriteFragment
import com.applifehack.knowledge.ui.activity.home.HomeActivity
import com.applifehack.knowledge.ui.activity.setting.SettingActivity
import com.applifehack.knowledge.ui.adapter.FeedAdapter
import com.applifehack.knowledge.ui.widget.listener.NavListener
import com.applifehack.knowledge.ui.widget.listener.QuoteViewListener
import com.applifehack.knowledge.ui.widget.listener.ToolbarQuoteListener
import com.applifehack.knowledge.util.extension.openLink
import com.ezyplanet.core.ui.listener.OnSnapPositionChangeListener
import com.ezyplanet.core.ui.widget.pager.SnapOnScrollListener
import com.ezyplanet.core.util.extension.attachSnapHelperWithListener
import kotlinx.android.synthetic.main.fragment_daily_feed.*

class QuotesActivity : BaseActivity<ActivityQuoteBinding, QuotesVM>(), QuotesNav,
    QuoteViewListener, ToolbarQuoteListener, NavListener {

    override val viewModel: QuotesVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_quote


    override fun onViewInitialized(binding: ActivityQuoteBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this
        viewModel.initData()

        try {

            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(daily_feed_recyclerview)
            daily_feed_recyclerview.attachSnapHelperWithListener(snapHelper,
                SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
                object : OnSnapPositionChangeListener {
                    override fun onSnapPositionChange(position: Int) {


                        viewModel.onPageChange(position)
                    }
                })

        } catch (ex: Exception) {

        }

        binding.adapter = FeedAdapter(viewModel, false)
        binding.listener = this
        binding.dailyFeedToolbar?.setListner(this)


        binding.listener = this
        observe(viewModel.results) {
            binding.adapter?.swapItems(it)
        }
        val event = "explore_quote"
        fbAnalytics.logEvent(event, event, "app_sections")

    }


    override fun openAuthorWiki(link: String?) {
        openLink(link!!, customTabHelper)

    }


    override fun selectCat(item: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sortBy(item: String) {
        viewModel.getQuotes(false, item.toLowerCase())
    }

    override fun back() {
        finish()
    }

    override fun onMenu() {

    }

    override fun onSetting() {
        gotoActivity(SettingActivity::class)
    }

    override fun onHome() {
        gotoActivity(HomeActivity::class, mapOf(HomeActivity.KEY_GO_HOME to true), true)

    }

    override fun onSaved() {


        gotoActivity(HomeActivity::class, mapOf(HomeActivity.KEY_GO_FAVORITE to true), true)
       // gotoActivity(FavoriteFragment::class)


}

override fun onCategory() {
    gotoActivity(HomeActivity::class, true)
}



}