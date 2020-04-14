package com.applifehack.knowledge.ui.fragment.slidepost

import com.ezyplanet.core.ui.base.MvvmNav
import com.applifehack.knowledge.data.entity.Post

interface SlidePostNav : MvvmNav{
    fun gotoPostDetail(post: Post)
    fun gotoCatDetail(catId:String)
}