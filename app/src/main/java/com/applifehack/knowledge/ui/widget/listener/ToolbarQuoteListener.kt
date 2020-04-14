package com.applifehack.knowledge.ui.widget.listener

import com.applifehack.knowledge.util.SortBy

interface ToolbarQuoteListener :ToolbarListener{
    fun selectCat(item:String)
    fun sortBy(sortBy: String)
    fun back()
}