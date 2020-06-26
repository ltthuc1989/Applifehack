package com.applifehack.knowledge.util

import android.view.View
import androidx.databinding.BindingAdapter


class BindingAdapters{
    companion object {
        @JvmStatic
        @BindingAdapter("viewSelected")
        fun setViewSelected(view: View, selected:Boolean) {
            view.isSelected = selected

        }
    }


}