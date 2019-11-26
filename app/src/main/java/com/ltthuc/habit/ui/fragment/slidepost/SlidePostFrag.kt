package com.ltthuc.habit.ui.fragment.slidepost

import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.ui.base.MvvmFragment
import com.ezyplanet.core.ui.base.ViewModelScope
import com.ezyplanet.core.util.extension.lazyFast
import com.ezyplanet.core.util.extension.observe
import com.ezyplanet.core.util.extension.putArgs
import com.ltthuc.habit.R
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.databinding.ItemSlidePostBinding
import com.ltthuc.habit.ui.activity.category.CategoryVM
import com.ltthuc.habit.util.extension.gotoPostDetail

class SlidePostFrag :MvvmFragment<SlidePostVM,ItemSlidePostBinding>(),SlidePostNav{

    companion object {
        const val key = "position"
        val tag = this::class.java.simpleName
    }
    override val viewModel :SlidePostVM by getLazyViewModel(ViewModelScope.FRAGMENT)
    override val layoutId :Int= R.layout.item_slide_post
    val catVM:CategoryVM by getLazyViewModel(ViewModelScope.ACTIVITY)
    fun newInstance(position:Int)=putArgs {
        putInt(key,position)

    }
    private val postion: Int by lazyFast {
        val args = arguments ?: throw IllegalStateException("Missing arguments!")
        args.getInt(key)
    }
    override fun setUpNavigator() {
        viewModel.navigator=this
    }

    override fun onViewInitialized(binding: ItemSlidePostBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        observe(catVM.getSlidePostItem(postion)){
            binding.item= it

        }



    }

    override fun gotoPostDetail(post: Post) {
        catVM.postPopularClick(post)
    }

    override fun gotoCatDetail(catId: String) {

    }
}