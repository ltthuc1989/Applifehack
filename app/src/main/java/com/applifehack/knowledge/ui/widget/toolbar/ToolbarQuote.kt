package com.applifehack.knowledge.ui.widget.toolbar

import android.content.Context
import android.opengl.Visibility
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.applifehack.knowledge.R
import com.applifehack.knowledge.databinding.ToolbarQuoteBinding
import com.applifehack.knowledge.ui.activity.quotes.QuotesVM
import com.applifehack.knowledge.ui.widget.listener.ToolbarQuoteListener
import com.applifehack.knowledge.util.AlertDialogUtils
import com.applifehack.knowledge.util.JsonHelper
import com.applifehack.knowledge.util.SortBy
import com.applifehack.knowledge.util.extension.toArray
import com.ezyplanet.core.util.extension.fromJson
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_quote_list.view.*
import kotlinx.android.synthetic.main.toolbar_quote.*

class ToolbarQuote : FrameLayout {
    lateinit var binding: ToolbarQuoteBinding

    private var isShowTitle = true
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
    var _viewModel :QuotesVM?=null
    var viewModel :QuotesVM?
        get() = _viewModel
        set(value){
            _viewModel = value
            binding.viewModel = value
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
            val quote= viewModel?.quotes?.value

            if(quote!=null) {
                AlertDialogUtils.showSingleChoice(context, _sortBy, quote!!, indexSortBy) {
                    indexSortBy = it
                    binding.listener?.sortBy(
                        quote?.get(it)
                    )
                    binding.tvSortBy.text = quote?.get(it)
                    _sortBy = quote?.get(it)
                    binding.tvTitle.visibility =
                        if (_sortBy.isNullOrEmpty() || _sortBy == "All") View.VISIBLE else View.GONE
                }
            }
        }



    }


    fun setListner(listener: ToolbarQuoteListener) {
        binding.listener = listener
    }
    fun setTitle(title:String?){
        title?.let {
            binding.tvTitle.text = it
        }

    }



}