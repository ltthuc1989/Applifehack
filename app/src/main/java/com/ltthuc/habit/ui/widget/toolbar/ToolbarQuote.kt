package com.ltthuc.habit.ui.widget.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.ltthuc.habit.R
import com.ltthuc.habit.databinding.ToolbarCategoryDetailBinding
import com.ltthuc.habit.databinding.ToolbarQuoteBinding
import com.ltthuc.habit.ui.widget.listener.ToolbarListener
import com.ltthuc.habit.ui.widget.listener.ToolbarQuoteListener
import com.ltthuc.habit.util.AlertDialogUtils

class ToolbarQuote : FrameLayout {
    lateinit var binding: ToolbarQuoteBinding

    private var _category:String?=null
    var category: String?
        get() = _category
        set(value) {
            _category = value
            binding.category = _category
        }

    private var _sortBy:String?=null
    var sortBy: String?
        get() = _sortBy
        set(value) {
            _sortBy = value
            binding.sortBy = _sortBy
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
        binding = ToolbarQuoteBinding.inflate(inflater, this, true)
        binding.tvSortBy.setOnClickListener {
            AlertDialogUtils.showSingleChoice(context,_sortBy,R.array.sortBy){
                binding.listener?.sortBy(it)
                binding.tvSortBy.text = it
            }
        }



    }


    fun setListner(listener: ToolbarQuoteListener) {
        binding.listener = listener
    }
}