package com.applifehack.knowledge.util

import android.app.ActivityOptions
import android.view.View
import com.ezyplanet.core.ui.base.MvvmActivity

object AnimationUtils {

    fun transition(mActivity: MvvmActivity<*,*>, shareView : View, shareName:String){
        val options = ActivityOptions.makeSceneTransitionAnimation(mActivity, shareView, shareName)
        mActivity.startActivity(mActivity.intent, options.toBundle())
    }
}