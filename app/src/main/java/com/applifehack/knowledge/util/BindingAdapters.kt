package com.applifehack.knowledge.util

import android.view.View
import android.widget.RelativeLayout
import androidx.databinding.BindingAdapter
import com.applifehack.knowledge.R


class BindingAdapters{
    companion object {
        @JvmStatic
        @BindingAdapter("viewSelected")
        fun setViewSelected(view: View, selected:Boolean) {
            view.isSelected = selected

        }

        @JvmStatic
        @BindingAdapter("viewHide")
        fun setViewHide(view: View, shareType: ShareType) {
            if(shareType!=ShareType.NONE) {
                (view.layoutParams as RelativeLayout.LayoutParams).apply {
                   height = if(shareType == ShareType.SHARING) 0 else
                       view.resources.getDimension(R.dimen.dp_16).toInt()
                }
            }

        }
    }


}