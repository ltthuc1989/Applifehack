package com.applifehack.knowledge.ui.activity.dynamiclink

import com.applifehack.knowledge.data.entity.Post
import com.ezyplanet.core.ui.base.MvvmNav

interface DynamicLinkNav : MvvmNav{
    fun openFeedFragment(post:Post?)
}