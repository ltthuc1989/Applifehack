package com.applifehack.knowledge.ui.admin.rssposts

import androidx.appcompat.widget.Toolbar
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.ui.base.adapter.SingleLayoutAdapter
import com.ezyplanet.core.util.extension.getExtraParcel
import com.ezyplanet.core.util.extension.observe
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.entity.Post

import com.applifehack.knowledge.databinding.ActivityRssListPostBinding
import com.applifehack.knowledge.databinding.ItemListPostBinding
import kotlinx.android.synthetic.main.activity_rss_list_post.*

class RssListPostActivity : MvvmActivity<ActivityRssListPostBinding, RssListPostVM>(), RssListPostNav {

    override val viewModel: RssListPostVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_rss_list_post

    companion object {
        const val KEY_FEED_URL = "FEED_URL"
    }

    override fun onViewInitialized(binding: ActivityRssListPostBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this

        viewModel.setModel(getExtraParcel(KEY_FEED_URL)!!)

        binding.adapter = SingleLayoutAdapter<Post, ItemListPostBinding>(
            R.layout.item_list_post,
            emptyList(),
            viewModel
        )

        observe(viewModel.results) {
            binding.adapter?.swapItems(it)
        }
        setToolBar(toolbarRssPost)
    }

    override fun resetState() {
        recyclerView.scrollToPosition(0)
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