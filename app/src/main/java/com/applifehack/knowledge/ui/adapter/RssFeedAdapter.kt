package com.applifehack.knowledge.ui.adapter

import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.ui.base.adapter.SingleLayoutAdapter
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.network.response.RssCatResp
import com.applifehack.knowledge.databinding.ItemFeedListBinding
import com.applifehack.knowledge.databinding.ItemRssLinkBinding

class RssFeedAdapter(viewModel: BaseViewModel<*, *>) : SingleLayoutAdapter<RssCatResp, ItemRssLinkBinding>(R.layout.item_rss_link,
    emptyList(),
    viewModel) {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int =
        when (items[position].type) {
            "video"->{
                ITEM_VIDEO
            }
            else ->{
                ITEM_ARTICLE
            }


        }


    companion object {

        private val ITEM_ARTICLE = R.layout.item_rss_link
        private val ITEM_VIDEO = R.layout.item_rss_youtube

    }

    fun getRowData(position: Int):RssCatResp{
        return items?.get(position)
    }

}