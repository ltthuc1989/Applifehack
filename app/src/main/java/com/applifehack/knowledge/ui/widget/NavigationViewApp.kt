package com.applifehack.knowledge.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.navigation.NavigationView
import com.applifehack.knowledge.databinding.NavViewBinding
import com.applifehack.knowledge.ui.widget.listener.NavListener

class NavigationViewApp : NavigationView{
    lateinit var binding :NavViewBinding

    constructor(context: Context?) : super(context) {
        initInflate()

    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initInflate()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initInflate()
    }

    private fun initInflate(){
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = NavViewBinding.inflate(inflater)
        addView(binding.root)



    }
    fun setListner(listener: NavListener){
        binding.listener=listener
    }
}
