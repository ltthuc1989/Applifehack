package com.applifehack.knowledge.ui.activity.quotes

import android.view.View
import com.ezyplanet.core.ui.base.MvvmNav

interface QuotesNav :MvvmNav{
    fun shareImage(view: View)
    fun openAuthorWiki(link:String?)
}