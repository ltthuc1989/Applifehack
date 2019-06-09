package com.ezyplanet.core.ui.base.adapter


import androidx.databinding.ViewDataBinding
import com.ezyplanet.core.ui.base.BaseViewModel

/**
 * Simplest implementation of [MvvmAdapter] to use as initView single layout adapter.
 */
open class SingleLayoutAdapter<T, B : ViewDataBinding>(
        private val layoutId: Int,
        items: List<T>,
        viewModel: BaseViewModel<*,*>? = null,
        onBind: B.(Int) -> Unit = {},listener:OnItemClickListener<T>?=null
) : MvvmAdapter<T, B>(viewModel = viewModel, items = items, onBind = onBind,listener=listener) {

    override fun getLayoutId(position: Int): Int = layoutId

}