package com.applifehack.knowledge.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.ui.base.adapter.SingleLayoutAdapter
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.databinding.ItemFeedListBinding
import com.applifehack.knowledge.databinding.ItemVideoListBinding
import com.ezyplanet.core.ui.base.adapter.MvvmViewHolder
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class FeedAdapter(viewModel: BaseViewModel<*, *>,private val isInFeed:Boolean=true, val lifecycle: Lifecycle? = null) : SingleLayoutAdapter<Post, ViewDataBinding>(R.layout.item_feed_list,
emptyList(),
viewModel) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MvvmViewHolder<Post, ViewDataBinding> {
         val holder = if (viewType != ITEM_VIDEO) {
             super.onCreateViewHolder(parent, viewType)
         } else {
             val inflater = LayoutInflater.from(parent.context)
             val binding: ItemVideoListBinding = DataBindingUtil.inflate(inflater, viewType, parent, false)
             YouTubePlayerViewHolder<Post, ItemVideoListBinding>(lifecycle, binding)
         }
        return holder
    }

    override fun onBindViewHolder(holder: MvvmViewHolder<Post, ViewDataBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is YouTubePlayerViewHolder<*,*>) {
            getItem(position).getVideoId()?.let { holder.cueVideo(it) }

        }
        Log.d("FeedAdapter","onBindViewHolder: $position")
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int =
        when (items[position].getPostType()) {
            PostType.ARTICLE -> {
                ITEM_ARTICLE
            }
            PostType.QUOTE -> {
                if(isInFeed) ITEM_QUOTE else ITEM_QUOTES
            }
            PostType.VIDEO -> {
                ITEM_VIDEO
            }

        }


    companion object {

        private val ITEM_ARTICLE = R.layout.item_feed_list
        private val ITEM_VIDEO = R.layout.item_video_list
        private val ITEM_QUOTE = R.layout.item_quote_list
        private val ITEM_QUOTES = R.layout.item_quotes_list
    }

    fun getRowData(position: Int):Post{
        return items!!.get(position)
    }


    internal class YouTubePlayerViewHolder<T, B>(
        lifecycle: Lifecycle?, binding: ItemVideoListBinding
    ) : MvvmViewHolder<Post,ItemVideoListBinding>(binding) {
        private var youTubePlayer: YouTubePlayer? = null
        private var currentVideoId: String? = null
        private var _isPlaying = false

        init {
            val youTubePlayerView = binding.youtubePlayerView
            lifecycle?.addObserver(youTubePlayerView)

            // the overlay view is used to intercept clicks when scrolling the recycler view
            // without it touch events used to scroll might accidentally trigger clicks in the player
            // when the overlay is clicked it starts playing the video
            binding.overlayView.setOnClickListener {
                youTubePlayer?.play()
                _isPlaying = true
            }

            youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    // store youtube player reference for later
                    this@YouTubePlayerViewHolder.youTubePlayer = youTubePlayer
                    // cue the video if it's available
                    currentVideoId?.let { youTubePlayer.cueVideo(it, 0f) }
                }

                override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                    when (state) {
                        // when the video is CUED, show the overlay.
                        PlayerConstants.PlayerState.VIDEO_CUED -> binding.overlayView.visibility = View.VISIBLE
                        // remove the overlay for every other state, so that we don't intercept clicks and the
                        // user can interact with the player.
                        else -> {
                            if (state == PlayerConstants.PlayerState.PLAYING) {
                                _isPlaying = true
                            } else if (state == PlayerConstants.PlayerState.PAUSED) {
                                _isPlaying = false
                            }
                            binding.overlayView.visibility = View.GONE
                        }
                    }
                }
            })
        }

        fun cueVideo(videoId: String) {
            currentVideoId = videoId
            // cue the video if the youtube player is available
            youTubePlayer?.cueVideo(videoId, 0f)
        }

        fun pause() {
            youTubePlayer?.pause()
            _isPlaying = false
        }

        fun isPlaying(): Boolean {
            return  _isPlaying
        }
    }

}