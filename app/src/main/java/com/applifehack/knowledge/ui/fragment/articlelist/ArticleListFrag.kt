package com.applifehack.knowledge.ui.fragment.articlelist

import com.ezyplanet.core.ui.base.ViewModelScope
import com.ezyplanet.core.ui.base.adapter.SingleLayoutAdapter
import com.ezyplanet.core.util.extension.observe
import com.ezyplanet.core.util.extension.putArgs
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.network.response.CatResp
import com.applifehack.knowledge.databinding.FragArticleListBinding
import com.applifehack.knowledge.databinding.ItemCatTopicBinding
import com.applifehack.knowledge.ui.activity.BaseActivity
import com.applifehack.knowledge.ui.fragment.BaseFragment
import com.applifehack.knowledge.ui.fragment.category.CategoryFrag


class ArticleListFrag : BaseFragment< FragArticleListBinding,ArticleListVM>(), ArticleListNav {

    override val viewModel: ArticleListVM by getLazyViewModel(ViewModelScope.FRAGMENT)
    override val layoutId: Int = R.layout.frag_article_list
    private var cat:CatResp?=null

    override fun setUpNavigator() {
        viewModel.navigator = this
    }
    companion object {
        fun newInstance(cat:CatResp) = ArticleListFrag().putArgs {
            putParcelable(CategoryFrag.KEY_CATEGORY_DETAIL,cat)
        }
    }
    override fun onViewInitialized(binding: FragArticleListBinding) {
        super.onViewInitialized(binding)
        viewModel.also { binding.viewModel = it }


           arguments?.getParcelable<CatResp>(CategoryFrag.KEY_CATEGORY_DETAIL)?.let {
               cat = it
               viewModel.updateModel(it.cat_id)
           }





        binding.adapter = SingleLayoutAdapter<Post, ItemCatTopicBinding>(
                R.layout.item_cat_topic,
                emptyList(),
                viewModel
        )


        observe(viewModel.results) {
            binding.adapter?.swapItems(it!!)
        }

        val event = "explore_article"
        fbAnalyticsHelper.logEvent(event,event,"app_sections")
    }



    override fun gotoPostDetail(post: Post) {
       openLink(post?.redirect_link)
    }

    override fun share(message: String) {
        (activity as BaseActivity<*, *>).shareMss(message)
    }

    override fun like(data: Post) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}