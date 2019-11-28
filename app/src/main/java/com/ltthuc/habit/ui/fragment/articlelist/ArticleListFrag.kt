package com.ltthuc.habit.ui.fragment.articlelist

import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.ui.base.MvvmFragment
import com.ezyplanet.core.ui.base.ViewModelScope
import com.ezyplanet.core.ui.base.adapter.SingleLayoutAdapter
import com.ezyplanet.core.util.extension.gotoActivity
import com.ezyplanet.core.util.extension.observe
import com.google.firebase.iid.FirebaseInstanceId
import com.ltthuc.habit.R
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.databinding.FragArticleListBinding
import com.ltthuc.habit.databinding.ItemCatTopicBinding


class ArticleListFrag : MvvmFragment<ArticleListVM, FragArticleListBinding>(), ArticleListNav {

    override val viewModel: ArticleListVM by getLazyViewModel(ViewModelScope.FRAGMENT)
    override val layoutId: Int = R.layout.frag_article_list
    override fun setUpNavigator() {
        viewModel.navigator = this
    }
    override fun onViewInitialized(binding: FragArticleListBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel


        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(activity!!) { it1 ->
            viewModel.getRssCat()
        }



        binding.adapter = SingleLayoutAdapter<Post, ItemCatTopicBinding>(
                R.layout.item_cat_topic,
                emptyList(),
                viewModel
        )

        observe(viewModel.results) {
            binding.adapter?.swapItems(it)
        }
    }



    override fun gotoPostDetail(post: Post) {

    }
}