package com.applifehack.knowledge.ui.fragment.videolist

import com.ezyplanet.core.ui.base.MvvmNav
import com.applifehack.knowledge.data.entity.Post

interface VideoListNav : MvvmNav{
    fun openYoutube(post:Post)
}