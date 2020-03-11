package com.ltthuc.habit.ui.activity.splash

import com.ezyplanet.core.ui.base.MvvmNav
import com.ltthuc.habit.data.firebase.PayloadResult

interface SplashNav : MvvmNav{
    fun gotoHomeScreen(data:PayloadResult?)
}