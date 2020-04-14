package com.applifehack.knowledge.ui.fragment.category

import com.ezyplanet.core.ui.base.MvvmNav
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.network.response.CatResp

interface CategoryNav : MvvmNav{

    fun gotoCatDetailScreen(resp: CatResp)
    fun gotoPostDetail(post: Post)
    fun openYoutube(link:String?)
}