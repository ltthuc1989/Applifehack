package com.applifehack.knowledge.ui.fragment.articlelist

import com.ezyplanet.core.ui.base.MvvmNav
import com.applifehack.knowledge.data.entity.Post

interface ArticleListNav : MvvmNav{
    fun gotoPostDetail(post: Post)
    fun share(message:String)
    fun like(data: Post)
}