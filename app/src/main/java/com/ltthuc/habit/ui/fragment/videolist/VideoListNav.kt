package com.ltthuc.habit.ui.fragment.videolist

import com.ezyplanet.core.ui.base.MvvmNav

interface VideoListNav : MvvmNav{
    fun openYoutube(link:String?)
}