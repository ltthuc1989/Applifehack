package com.ezyplanet.core.ui.listener

import android.os.SystemClock
import android.util.Log
import android.view.View

class OneClickListener(
        private val onClickListener: View.OnClickListener, private var defaultInterval: Int = 1000
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        Log.d("oneClick","oneClick")
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        Log.d("oneClicked","oneClick")
        lastTimeClicked = SystemClock.elapsedRealtime()
        onClickListener.onClick(v)
    }
}