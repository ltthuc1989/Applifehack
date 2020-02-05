package com.ezyplanet.core.ui.widget

import android.app.Dialog
import android.content.Context
import android.view.Display
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.ezyplanet.core.R

import com.github.ybq.android.spinkit.style.Wave

class TransparentProgressDialog(context: Context, display: Display) : Dialog(context, R.style.TransparentProgressDialog) {


    init {
        val wlmp = window!!.attributes
        wlmp.gravity = Gravity.CENTER_HORIZONTAL


        window!!.attributes = wlmp
        setTitle(null)
        setCancelable(true)
        setOnCancelListener(null)
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        val params = LinearLayout.LayoutParams(display.width / 8, display.width / 8)
        val progressBar = ProgressBar(getContext())
        val wave = Wave()
        wave.color = getContext().resources.getColor(R.color.white)
        progressBar.indeterminateDrawable = wave
        layout.addView(progressBar, params)
        addContentView(layout, params)

    }

    override fun show() {
        super.show()
    }
}