package com.applifehack.knowledge.ui.widget.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.applifehack.knowledge.databinding.ToolbarBinding

import com.applifehack.knowledge.ui.widget.listener.ToolbarListener


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

    fun showRefresh(show:Boolean){
        binding.imMenu.visibility = if(show) View.VISIBLE else View.GONE
    }
    fun setDatePost(date:String){
        binding.tvPostedDate.text =  date
    }
    fun showDate(show:Boolean){
        binding.tvPostedDate.visibility = if(show) View.VISIBLE else View.GONE
    }


}