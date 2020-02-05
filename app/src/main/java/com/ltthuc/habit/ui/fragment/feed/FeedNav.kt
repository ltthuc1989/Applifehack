package com.ltthuc.habit.ui.fragment.feed

import android.view.View
import android.widget.ImageView
import com.ezyplanet.core.ui.base.MvvmNav
import com.ltthuc.habit.data.entity.Post


interface FeedNav : MvvmNav{
    fun gotoFeedDetail(post: Post)
    fun gotoCatDetail(id:String?)
    fun gotoPageUrl(post: Post)
    fun share(message:String)
    fun shareImage(view: View)
    fun gotoYoutubeDetail(post: Post,shareView:View)

}