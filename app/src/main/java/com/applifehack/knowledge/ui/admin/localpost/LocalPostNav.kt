package com.applifehack.knowledge.ui.admin.localpost

import android.view.View
import com.applifehack.knowledge.data.entity.Post
import com.ezyplanet.core.ui.base.MvvmNav

interface LocalPostNav :MvvmNav{
    fun gotoFeedDetail(post: Post)
    fun gotoYoutubeDetail(post: Post, shareView: View)
    fun scrollToTop()
    fun openDetail(post:Post)
}