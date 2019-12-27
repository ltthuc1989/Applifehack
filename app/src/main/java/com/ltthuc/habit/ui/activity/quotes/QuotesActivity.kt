package com.ltthuc.habit.ui.activity.quotes

import android.view.View
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.PagerSnapHelper
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.ui.base.adapter.MvvmAdapter
import com.ezyplanet.core.util.extension.gotoActivity
import com.ezyplanet.core.util.extension.observe
import com.ltthuc.habit.R
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.databinding.ActivityQuoteBinding
import com.ltthuc.habit.ui.activity.category.CategoryActivity
import com.ltthuc.habit.ui.activity.feed.FeedActivity
import com.ltthuc.habit.ui.adapter.FeedAdapter
import com.ltthuc.habit.ui.widget.QuoteView
import com.ltthuc.habit.ui.widget.listener.NavListener
import com.ltthuc.habit.ui.widget.listener.QuoteViewListener
import com.ltthuc.habit.ui.widget.listener.ToolbarQuoteListener
import com.ltthuc.habit.util.CustomTabHelper
import com.ltthuc.habit.util.SortBy
import com.ltthuc.habit.util.extension.openLink
import com.ltthuc.habit.util.extension.shareMessage
import kotlinx.android.synthetic.main.activity_daily_feed.*

class QuotesActivity : MvvmActivity<ActivityQuoteBinding, QuotesVM>(), QuotesNav,
        NavListener, QuoteViewListener, ToolbarQuoteListener ,MvvmAdapter.OnItemClickListener<Post>{

    override val viewModel: QuotesVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_quote

    private var customTabHelper: CustomTabHelper = CustomTabHelper()

    override fun onViewInitialized(binding: ActivityQuoteBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this
        viewModel.getQuotes(false)

        try {

            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(daily_feed_recyclerview)


        } catch (ex: Exception) {

        }

        binding.adapter = FeedAdapter(viewModel, false)
        binding.listener = this
        binding.dailyFeedToolbar?.setListner(this)
        binding?.adapter?.listener = this

        binding.homeNavigationView.setListner(this)
        observe(viewModel.results) {
            binding.adapter?.swapItems(it)
        }


    }

    override fun onItemClick(view: View, item: Post, position: Int) {
        val view = view.rootView.findViewById<QuoteView>(R.id.quoteView)
        val quote = view.getQuote()
        viewModel.generataQuote(this,quote)
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

    override fun openAuthorWiki(link: String?) {
        openLink(link!!, customTabHelper)

    }

    override fun share(message: String) {
        shareMessage(message)
    }

    override fun selectCat(item: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sortBy(item: SortBy) {
        viewModel.getQuotes(false,item)
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

}