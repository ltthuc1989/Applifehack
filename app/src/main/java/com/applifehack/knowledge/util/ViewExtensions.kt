package com.applifehack.knowledge.util

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewParent
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import org.sourcei.kowts.utils.functions.F

// change layout parameters
fun View.setParams(width: Int, height: Int) {
    val parent: ViewParent = parent
    if (parent is FrameLayout)
        layoutParams = FrameLayout.LayoutParams(width, height)
    if (parent is RelativeLayout)
        layoutParams = RelativeLayout.LayoutParams(width, height)
    if (parent is LinearLayout)
        layoutParams = LinearLayout.LayoutParams(width, height)
}

// set gradient on view
fun View.setGradient(colors: IntArray, radius: Int = 0, angle: Float = 0F) {

    val bbg = if (background != null)
        background.current as GradientDrawable
    else
        GradientDrawable()

    bbg.cornerRadius = F.dpToPx(radius, context).toFloat()
    bbg.colors = colors

    bbg.orientation = when (angle) {
        45f -> GradientDrawable.Orientation.BL_TR
        90f -> GradientDrawable.Orientation.BOTTOM_TOP
        135f -> GradientDrawable.Orientation.BR_TL
        180f -> GradientDrawable.Orientation.RIGHT_LEFT
        225f -> GradientDrawable.Orientation.TR_BL
        270f -> GradientDrawable.Orientation.TOP_BOTTOM
        315f -> GradientDrawable.Orientation.TL_BR
        // for 0f
        else -> GradientDrawable.Orientation.LEFT_RIGHT
    }
    background = bbg
}