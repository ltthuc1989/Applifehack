package com.applifehack.knowledge.ui.fragment.slidepost

import androidx.lifecycle.ViewModelProvider
import com.ezyplanet.core.ui.base.MvvmFragment
import com.ezyplanet.core.ui.base.ViewModelScope
import com.ezyplanet.core.util.extension.lazyFast
import com.ezyplanet.core.util.extension.observe
import com.ezyplanet.core.util.extension.putArgs
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.databinding.ItemSlidePostBinding
import com.applifehack.knowledge.ui.fragment.category.CategoryShareEvent
import com.applifehack.knowledge.ui.fragment.category.CategoryVM

class SlidePostFrag :MvvmFragment<SlidePostVM,ItemSlidePostBinding>(),SlidePostNav{

    companion object {
        const val key = "position"
        val tag = this::class.java.simpleName
    }
    override val viewModel :SlidePostVM by getLazyViewModel(ViewModelScope.FRAGMENT)
    override val layoutId :Int= R.layout.item_slide_post
     lateinit var categoryShareEvent: CategoryShareEvent
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
        categoryShareEvent = ViewModelProvider(activity!!).get(CategoryShareEvent::class.java)




    }

    override fun gotoPostDetail(post: Post) {
        categoryShareEvent.postClickEvent.postValue(post)
    }

    override fun gotoCatDetail(catId: String) {
        categoryShareEvent.catClickEvent.postValue(catId)

    }
}