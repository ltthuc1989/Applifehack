package com.applifehack.knowledge.ui.activity

import androidx.appcompat.widget.Toolbar
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.ui.base.adapter.SingleLayoutAdapter
import com.ezyplanet.core.util.extension.gotoActivity
import com.ezyplanet.core.util.extension.observe
import com.google.firebase.iid.FirebaseInstanceId
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.network.response.RssCatResp
import com.applifehack.knowledge.databinding.ActivityHomeBinding
import com.applifehack.knowledge.databinding.ActivtyRssBinding
import com.applifehack.knowledge.databinding.ItemRssLinkBinding
import com.applifehack.knowledge.ui.activity.webview.WebViewActivity
import com.applifehack.knowledge.ui.adapter.FeedAdapter
import com.applifehack.knowledge.ui.adapter.RssFeedAdapter
import com.applifehack.knowledge.ui.admin.rssposts.RssListPostActivity
class RSSActivity : MvvmActivity<ActivtyRssBinding,RSSVM>(),RSSNav{

    override val viewModel: RSSVM by getLazyViewModel()
    override val layoutId: Int =R.layout.activty_rss

    override fun onViewInitialized(binding: ActivtyRssBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) { it1 ->
            viewModel.getRssCat()
        }



        binding.adapter = RssFeedAdapter(viewModel)

        observe(viewModel.results) {
            binding.adapter?.swapItems(it!!)
        }
        setToolBar(binding.toolbarRss)
    }

    override fun gotoListPostScreen(resp: RssCatResp) {
        gotoActivity(
                RssListPostActivity::class,
                mapOf(RssListPostActivity.KEY_FEED_URL to resp)
            )

    }

    private fun setToolBar(toolbar: Toolbar, title: String?="") {
        toolbar.setTitle(title)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}