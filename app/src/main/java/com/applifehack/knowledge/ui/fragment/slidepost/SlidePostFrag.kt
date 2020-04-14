package com.applifehack.knowledge.ui.fragment.slidepost

import com.ezyplanet.core.ui.base.MvvmFragment
import com.ezyplanet.core.ui.base.ViewModelScope
import com.ezyplanet.core.util.extension.lazyFast
import com.ezyplanet.core.util.extension.observe
import com.ezyplanet.core.util.extension.putArgs
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.databinding.ItemSlidePostBinding
import com.applifehack.knowledge.ui.fragment.category.CategoryVM

class SlidePostFrag :MvvmFragment<SlidePostVM,ItemSlidePostBinding>(),SlidePostNav{

    companion object {
        const val key = "position"
        val tag = this::class.java.simpleName
    }
    override val viewModel :SlidePostVM by getLazyViewModel(ViewModelScope.FRAGMENT)
    override val layoutId :Int= R.layout.item_slide_post
    val catVM:CategoryVM by getLazyViewModel(ViewModelScope.ACTIVITY)
    fun newInstance(post:Post)=putArgs {

        putParcelable(key,post)

    }

    override fun setUpNavigator() {
        viewModel.navigator=this
    }

    override fun onViewInitialized(binding: ItemSlidePostBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        binding.item = arguments?.getParcelable(key)




    }

    override fun gotoPostDetail(post: Post) {
        catVM.postPopularClick(post)
    }

    override fun gotoCatDetail(catId: String) {

    }
}