package com.applifehack.knowledge.ui.activity.quotes

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.PagerSnapHelper
import com.ezyplanet.core.util.extension.gotoActivity
import com.ezyplanet.core.util.extension.observe
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.entity.ArticleType
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.databinding.ActivityQuoteBinding
import com.applifehack.knowledge.ui.activity.BaseActivity
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

class QuotesActivity : BaseActivity<ActivityQuoteBinding, QuotesVM>(), QuotesNav,
    QuoteViewListener, ToolbarQuoteListener, NavListener {

    override val viewModel: QuotesVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_quote
    companion object {
        val KEY_CAT_TYPE = "CAT_TYPE"
        val KEY_CAT_NAME = "CAT_NAME"
    }


    override fun onViewInitialized(binding: ActivityQuoteBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this
        var catName = intent.getStringExtra(
            KEY_CAT_NAME)
        intent.getStringExtra(KEY_CAT_TYPE)?.let { viewModel.initData(it) }

        try {

            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(binding.dailyFeedRecyclerview)
            binding.dailyFeedRecyclerview.attachSnapHelperWithListener(snapHelper,
                SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
                object : OnSnapPositionChangeListener {
                    override fun onSnapPositionChange(position: Int) {

                        (binding?.adapter as FeedAdapter)?.getRowData(position)?.let {
                            it?.quote_type?.let { type->
                                if(catName==null) catName = ""
                                binding.dailyFeedToolbar.setTitle("$type")
                            }

                        }


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
            binding.adapter?.swapItems(it!!)
        }
        val event = "explore_quote"
        fbAnalytics.logEvent(event, event, "app_sections")
        viewModel.quotes.observe {
            if(it.isEmpty()){
                binding.dailyFeedToolbar.findViewById<TextView>(R.id.tvSortBy).visibility = View.GONE
            }else{
                binding.dailyFeedToolbar.findViewById<TextView>(R.id.tvSortBy).visibility = View.VISIBLE
            }
        }

    }


    override fun openAuthorWiki(link: String?) {
        openLink(link!!, customTabHelper)

    }


    override fun selectCat(item: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sortBy(item: String) {
        viewModel.getPosts(false, item)
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

    override fun gotoPageUrl(isAuthor:Boolean,post: Post) {
        var url :String? = null
        if(isAuthor &&post.authorUrl.isNullOrEmpty()) return
        if(isAuthor && !post.authorUrl.isNullOrEmpty()) {
            url = post.authorUrl
        }
        if(!isAuthor) url = post.redirect_link

        if(!url.isNullOrEmpty()) openBrowser(url)

    }

    override fun onCategory() {
    gotoActivity(HomeActivity::class, true)
}

    override fun scrollToTop() {
        binding.dailyFeedRecyclerview?.scrollToPosition(0)
    }


}