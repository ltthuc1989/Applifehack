package com.applifehack.knowledge.ui.adapter

import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.ui.base.adapter.SingleLayoutAdapter
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.databinding.ItemFeedListBinding

class FeedAdapter(viewModel: BaseViewModel<*, *>, private val isInFeed: Boolean = true) :
    SingleLayoutAdapter<Post, ItemFeedListBinding>(
        R.layout.item_feed_list,
        emptyList(),
        viewModel
    ) {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int =
        when (items[position].getPostType()) {
            PostType.ARTICLE -> {
                ITEM_ARTICLE
            }
            PostType.QUOTE -> {
                if (isInFeed) ITEM_QUOTE else ITEM_QUOTES
            }
            PostType.VIDEO -> {
                ITEM_VIDEO
            }
            PostType.PICTURE -> {
                if (isInFeed) {

                    ITEM_ARTICLE
                } else ITEM_HACKS
            }
            PostType.FACT, PostType.HACK -> {
                val post = items[position]
                if (post.imageUrl.isNullOrEmpty()) {
                    if (isInFeed) ITEM_QUOTE else ITEM_QUOTES
                } else {
                    if (isInFeed) ITEM_ARTICLE else ITEM_HACKS
                }

            }


        }


    companion object {

        private val ITEM_ARTICLE = R.layout.item_feed_list
        private val ITEM_VIDEO = R.layout.item_video_list
        private val ITEM_QUOTE = R.layout.item_quote_list
        private val ITEM_QUOTES = R.layout.item_quotes_list
        private val ITEM_HACKS = R.layout.item_feed_lists
    }

    fun getRowData(position: Int): Post? {
        if (position == -1) return null
        return items?.get(position)
    }

}