package com.ltthuc.habit.ui.widget.listener

import com.ltthuc.habit.util.SortBy

interface ToolbarQuoteListener :ToolbarListener{
    fun selectCat(item:String)
    fun sortBy(sortBy: SortBy)
}