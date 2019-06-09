package com.ezyplanet.core.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar

import androidx.databinding.ViewDataBinding

abstract class MvvmToolbar<B : ViewDataBinding>  : Toolbar {
    lateinit var binding: B
     abstract val layoutToolbarId: Int

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