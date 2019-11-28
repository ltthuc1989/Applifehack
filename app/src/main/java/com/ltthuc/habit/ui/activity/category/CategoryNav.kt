package com.ltthuc.habit.ui.activity.category

import com.ezyplanet.core.ui.base.MvvmNav
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.data.network.response.CatResp
import com.ltthuc.habit.data.network.response.RssCatResp

interface CategoryNav : MvvmNav{

    fun gotoCatDetailScreen(resp: CatResp)
    fun gotoPostDetail(post: Post)
    fun openYoutube(link:String?)
}