package com.applifehack.knowledge.ui.activity.quotes

import android.view.View
import com.ezyplanet.core.ui.base.MvvmNav

interface QuotesNav :MvvmNav{
    fun openAuthorWiki(link:String?)
    fun scrollToTop()
}