package com.ltthuc.habit.ui.adapter

import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.ui.base.adapter.SingleLayoutAdapter
import com.ltthuc.habit.R
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.data.entity.PostType
import com.ltthuc.habit.databinding.ItemFeedListBinding

class FeedAdapter(viewModel: BaseViewModel<*, *>) : SingleLayoutAdapter<Post, ItemFeedListBinding>(R.layout.item_feed_list,
        emptyList(),
        viewModel) {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int =
            when (items[position].getPostType()) {
                PostType.ARTICLE -> {
                    ITEM_ARTICLE
                }
                PostType.QUOTE -> {
                    ITEM_QUOTE
                }
                PostType.VIDEO -> {
                    ITEM_ARTICLE
                }
                else -> {
                    ITEM_ARTICLE
                }
            }


    companion object {
        private val ITEM_ARTICLE = R.layout.item_feed_list
        private val ITEM_QUOTE = R.layout.item_quote_list
    }

}