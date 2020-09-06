package com.applifehack.knowledge.util

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout

import androidx.databinding.BindingAdapter

import org.sourcei.kowts.utils.functions.F
import org.sourcei.kowts.utils.reusables.Angles


class BindingAdapters{
    companion object {
        @JvmStatic
        @BindingAdapter("viewSelected")
        fun setViewSelected(view: View, selected:Boolean) {
            view.isSelected = selected

        }

        @JvmStatic
        @BindingAdapter("gradient")
        fun setGradient(view: View, selected:Boolean) {
            val colors = F.randomGradient().toIntArray()
            val angle = Angles.random().toFloat()
            view.setGradient(colors, 0, angle)
        }

        @JvmStatic
        @BindingAdapter("textGravity")
        fun setTextGravity(view: View, isBottom: Boolean) {

            ( view.layoutParams as FrameLayout.LayoutParams).apply {
                if(isBottom) this.gravity = Gravity.BOTTOM else this.gravity = Gravity.TOP
            }
        }
    }


}