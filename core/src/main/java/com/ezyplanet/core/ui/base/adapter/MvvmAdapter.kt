package com.ezyplanet.core.ui.base.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ezyplanet.core.BR
import com.ezyplanet.core.R
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.CoreConstants


/**
 * An abstract Adapter that extends [RecyclerView.Adapter] and can be used as base adapter in layouts provided by
 * DataBinding.
 *
 * @param itemBindingId Generated item binding id that will should be founded in BR class and will be
 * used in [MvvmViewHolder.bind] method.
 *
 * @param items list of items to be shown. Can be changed later by calling [swapItems].
 *
 * @param onBind an extension function on [B] that receives position of current item and allows us
 * to access binding class outside of [MvvmAdapter].
 *
 */
abstract class MvvmAdapter<T, B : ViewDataBinding>(
        private var itemBindingId: Int = BR.item,
        private var viewModelBindingId: Int = BR.viewModel,
        private var viewModel: BaseViewModel<*,*>?,
        var items: List<T>,
        var onBind: B.(Int) -> Unit = {},var listener:OnItemClickListener<T>?=null
) : RecyclerView.Adapter<MvvmViewHolder<T, B>>() {

    /**
     * get item at given position
     */


    protected fun isPositionFooter(position: Int): Boolean {
        return position > items.size

    }

    fun getItem(position: Int): T = items[position]


    override fun getItemCount(): Int{
        if(items.size>0&&items.size% CoreConstants.LIMIT_ITEM_PER_PAGE==0 &&viewModel?.isNoMoreDataLoad==false) {
            return items.size + 1
        }else{
            if(items.isEmpty()&&viewModel?.resetLoadingState!=true){
                return 1
            }
            return items.size
        }
    }

    /**
     * abstract function to decide which layout should be shown at given position.
     * This will be useful for multi layout adapters. for single layout adapter it can only returns
     * initView static layout resource id.
     *
     * @return relevant layout resource id based on given position
     *
     */
    abstract fun getLayoutId(position: Int): Int

    /**
     * Instead of returning viewType, this method will return layout id at given position provided
     * by [getLayoutId] and will be used in [onCreateViewHolder].
     *
     * @see [RecyclerView.Adapter.getItemViewType]
     */
    override fun getItemViewType(position: Int): Int {
         if (!items.isEmpty()&&position == items.size){
            return ITEM_PROGRESS
         }
        if (items.isEmpty()){
            return ITEM_EMPTY
        }

       return getLayoutId(position)
    }

    /**
     * Attempt to create an instance of [MvvmViewHolder] with inflated Binding class
     *
     * @param viewType will be used as layoutId for [DataBindingUtil] and will be provided by [getItemViewType]
     *
     * @see [RecyclerView.Adapter.onCreateViewHolder]
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MvvmViewHolder<T, B> {
        val inflater = LayoutInflater.from(parent.context)
        val binding: B = DataBindingUtil.inflate(inflater, viewType, parent, false)
        return MvvmViewHolder(binding)
    }

    /**
     * Attempt to bind item at given position to holder.
     * And also attempts to invoke [onBind] lambda
     * function on instance of [B] in [MvvmViewHolder.binding].
     *
     * @see [RecyclerView.Adapter.onBindViewHolder]
     */
    override fun onBindViewHolder(holder: MvvmViewHolder<T, B>, position: Int) {
        if(position==items.size) {
            if(items.isEmpty()){
              // holder.bind(viewModelBindingId, viewModel)
              // holder.binding.onBind(position)
            }
            return
        }
        listener?.let {
            val viewGroup = holder.binding.root as ViewGroup
            viewGroup.forEach {
                it?.setOnClickListener{
                    listener?.onItemClick(it,items[position],position)
                }
            }
            holder.binding.root.setOnClickListener {
                listener?.onItemClick(it,items[position],position)
            }
        }

        holder.bind(itemBindingId, getItem(position), viewModelBindingId, viewModel)
        holder.binding.onBind(position)
    }

    /**
     * Attempts to replace current list of items with newly provided items and notify adapter
     * based on differences of these two lists by [DiffUtil]
     */
    open fun swapItems(newItems: List<T>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    items[oldItemPosition] == newItems[newItemPosition]

            override fun getOldListSize(): Int =
                    items.size

            override fun getNewListSize(): Int =
                    newItems.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    items[oldItemPosition] == newItems[newItemPosition]
        })
        diffResult.dispatchUpdatesTo(this)

        // newItems.toList() provide initView new instance of list with different reference in memory
        // to prevent same instance of objects issues
        items = newItems.toList()
        notifyDataSetChanged()
    }

    /**
     * A default interface that can be used as click listener of items
     */
    interface OnItemClickListener<T> {
        fun onItemClick(view: View, item: T, position: Int = -1)
    }




    companion object {
        private val ITEM_PROGRESS = R.layout.item_progress
        private val ITEM_EMPTY= R.layout.item_empty_view
    }

}



