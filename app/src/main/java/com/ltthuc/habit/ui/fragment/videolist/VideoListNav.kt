package com.ltthuc.habit.ui.fragment.videolist

import com.ezyplanet.core.ui.base.MvvmNav
import com.ltthuc.habit.data.entity.Post

interface VideoListNav : MvvmNav{
    fun openYoutube(post:Post)
}