package com.ezyplanet.core.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.databinding.ViewDataBinding

abstract class MvvmWidget<B : ViewDataBinding> : FrameLayout {
    lateinit var binding: B

    constructor(context: Context?) : super(context) {
        initInflate()


    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initInflate()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initInflate()
    }


    abstract fun initInflate()




}