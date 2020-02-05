package com.ltthuc.habit.ui.fragment.articlelist

import android.view.View
import com.ezyplanet.core.ui.base.MvvmNav
import com.ltthuc.habit.data.entity.Post

interface ArticleListNav : MvvmNav{
    fun gotoPostDetail(post: Post)
    fun share(message:String)
    fun like(data: Post)
}