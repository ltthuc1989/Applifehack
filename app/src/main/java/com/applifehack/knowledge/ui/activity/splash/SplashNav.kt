package com.applifehack.knowledge.ui.activity.splash

import com.ezyplanet.core.ui.base.MvvmNav
import com.applifehack.knowledge.data.firebase.PayloadResult

interface SplashNav : MvvmNav{
    fun gotoHomeScreen(data:PayloadResult?)
}