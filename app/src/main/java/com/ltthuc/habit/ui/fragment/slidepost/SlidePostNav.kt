package com.ltthuc.habit.ui.fragment.slidepost

import com.ezyplanet.core.ui.base.MvvmNav
import com.ltthuc.habit.data.entity.Post

interface SlidePostNav : MvvmNav{
    fun gotoPostDetail(post: Post)
    fun gotoCatDetail(catId:String)
}