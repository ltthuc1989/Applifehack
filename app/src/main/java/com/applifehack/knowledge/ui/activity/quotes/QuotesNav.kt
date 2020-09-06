package com.applifehack.knowledge.ui.activity.quotes

import android.view.View
import com.applifehack.knowledge.data.entity.Post
import com.ezyplanet.core.ui.base.MvvmNav

interface QuotesNav :MvvmNav{
    fun openAuthorWiki(link:String?)
    fun scrollToTop()
    fun gotoPageUrl(isAuthor:Boolean,post: Post)
}