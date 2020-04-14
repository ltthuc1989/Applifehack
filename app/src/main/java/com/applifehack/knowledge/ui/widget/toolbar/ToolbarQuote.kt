package com.applifehack.knowledge.ui.widget.toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.applifehack.knowledge.R
import com.applifehack.knowledge.databinding.ToolbarQuoteBinding
import com.applifehack.knowledge.ui.widget.listener.ToolbarQuoteListener
import com.applifehack.knowledge.util.AlertDialogUtils
import com.applifehack.knowledge.util.JsonHelper
import com.applifehack.knowledge.util.SortBy
import com.applifehack.knowledge.util.extension.toArray
import com.ezyplanet.core.util.extension.fromJson
import com.google.gson.Gson

class ToolbarQuote : FrameLayout {
    lateinit var binding: ToolbarQuoteBinding

    private var _category:String?=null
    private var indexSortBy = 0
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
        val array = context.resources.getStringArray(R.array.quote_type)
        binding.tvSortBy.setOnClickListener {
            AlertDialogUtils.showSingleChoice(context,_sortBy, array,indexSortBy){
               indexSortBy =it
                binding.listener?.sortBy(
                       array[it]
                )
                binding.tvSortBy.text = array[it]
                _sortBy = array[it]
            }
        }



    }


    fun setListner(listener: ToolbarQuoteListener) {
        binding.listener = listener
    }


}