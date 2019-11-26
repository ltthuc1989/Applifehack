package com.ltthuc.habit.ui.activity.feed

import com.ezyplanet.core.ui.base.MvvmNav
import com.ltthuc.habit.data.entity.Post


interface FeedNav : MvvmNav{
    fun gotoFeedDetail(post: Post)
}