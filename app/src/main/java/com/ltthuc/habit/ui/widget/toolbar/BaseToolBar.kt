package com.ltthuc.habit.ui.widget.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.ltthuc.habit.databinding.ToolbarBinding

import com.ltthuc.habit.ui.widget.listener.ToolbarListener


class BaseToolBar :FrameLayout{
    lateinit var binding: ToolbarBinding


   private var _titleBar:String?=null
    var titleBar: String?
        get() = _titleBar
        set(value) {
            _titleBar = value
            binding.title.setText(value)
        }

    constructor(context: Context?) : super(context) {
        initInflate()

    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initInflate()

    }


    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initInflate()
    }

    fun initInflate() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ToolbarBinding.inflate(inflater, this, true)
        binding.imMenu.setOnClickListener {
            binding?.listener?.onMenu()
        }


    }




    fun setListner(listener: ToolbarListener) {
        binding.listener = listener
    }


}