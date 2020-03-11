package com.ltthuc.habit.ui.activity.home

import com.ezyplanet.core.ui.base.MvvmNav

interface HomeNav : MvvmNav{

    fun openArtilce(link:String?)
    fun openVideo(id:String?)
    fun openQuote()

}