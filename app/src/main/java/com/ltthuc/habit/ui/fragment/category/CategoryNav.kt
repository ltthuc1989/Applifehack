package com.ltthuc.habit.ui.fragment.category

import com.ezyplanet.core.ui.base.MvvmNav
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.data.network.response.CatResp

interface CategoryNav : MvvmNav{

    fun gotoCatDetailScreen(resp: CatResp)
    fun gotoPostDetail(post: Post)
    fun openYoutube(link:String?)
}