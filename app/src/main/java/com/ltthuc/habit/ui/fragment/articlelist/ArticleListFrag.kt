package com.ltthuc.habit.ui.fragment.articlelist

import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.ui.base.MvvmFragment
import com.ezyplanet.core.ui.base.ViewModelScope
import com.ezyplanet.core.ui.base.adapter.SingleLayoutAdapter
import com.ezyplanet.core.util.extension.observe
import com.ezyplanet.core.util.extension.putArgs
import com.ltthuc.habit.R
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.data.firebase.FirebaseAnalyticsHelper
import com.ltthuc.habit.data.network.response.CatResp
import com.ltthuc.habit.databinding.FragArticleListBinding
import com.ltthuc.habit.databinding.ItemCatTopicBinding
import com.ltthuc.habit.ui.activity.BaseActivity
import com.ltthuc.habit.ui.fragment.BaseFragment
import com.ltthuc.habit.ui.fragment.category.CategoryFrag
import com.ltthuc.habit.util.extension.openLink
import javax.inject.Inject


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
        binding.viewModel = viewModel


           arguments?.getParcelable<CatResp>(CategoryFrag.KEY_CATEGORY_DETAIL)?.let {
               cat = it
               viewModel.updateModel(it.id)
           }





        binding.adapter = SingleLayoutAdapter<Post, ItemCatTopicBinding>(
                R.layout.item_cat_topic,
                emptyList(),
                viewModel
        )


        observe(viewModel.results) {
            binding.adapter?.swapItems(it)
        }

        val event = "explore_article"
        fbAnalyticsHelper.logEvent(event,event,"app_sections")
    }



    override fun gotoPostDetail(post: Post) {
       openLink(post?.webLink)
    }

    override fun share(message: String) {
        (activity as BaseActivity<*, *>).shareMss(message)
    }

    override fun like(data: Post) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}