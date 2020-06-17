package com.applifehack.knowledge.ui.activity.setting

import com.ezyplanet.core.ui.base.MvvmNav

interface SettingNav : MvvmNav{
    fun gotoPolicy(url:String)
    fun sendFeedBack(email:String)
    fun rateUs()

}