package com.ltthuc.habit.ui.fragment.videolist

import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.ui.base.MvvmFragment
import com.ezyplanet.core.ui.base.ViewModelScope
import com.ezyplanet.core.ui.base.adapter.SingleLayoutAdapter
import com.ezyplanet.core.util.extension.gotoActivity
import com.ezyplanet.core.util.extension.observe
import com.ezyplanet.core.util.extension.putArgs
import com.ezyplanet.core.util.extension.transitionActivity
import com.ltthuc.habit.R
import com.ltthuc.habit.data.entity.ArticleType
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.data.firebase.FirebaseAnalyticsHelper
import com.ltthuc.habit.data.network.response.CatResp
import com.ltthuc.habit.databinding.FragVideoListBinding
import com.ltthuc.habit.databinding.ItemCatVideoBinding
import com.ltthuc.habit.ui.fragment.category.CategoryFrag
import com.ltthuc.habit.ui.activity.ytDetail.YtDetailActivity
import com.ltthuc.habit.util.AppBundleKey
import javax.inject.Inject

class VideoListFrag : MvvmFragment<VideoListVM, FragVideoListBinding>(), VideoListNav {

    override val viewModel: VideoListVM by getLazyViewModel(ViewModelScope.FRAGMENT)
    override val layoutId: Int = R.layout.frag_video_list
    private var isDataLoaded = false
    private var cat:CatResp?=null
    @Inject lateinit var fbAnalyticsHelper: FirebaseAnalyticsHelper
    override fun setUpNavigator() {
        viewModel.navigator = this
    }

    companion object {
        fun newInstance(cat:CatResp) = VideoListFrag().putArgs {
            putParcelable(CategoryFrag.KEY_CATEGORY_DETAIL,cat)
        }
    }

    override fun onViewInitialized(binding: FragVideoListBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel


        binding.adapter = SingleLayoutAdapter<Post, ItemCatVideoBinding>(
                R.layout.item_cat_video,
                emptyList(),
                viewModel
        )
       cat = arguments?.getParcelable(CategoryFrag.KEY_CATEGORY_DETAIL)

        observe(viewModel.results) {
            binding.adapter?.swapItems(it)
        }
        val event = "explore_video"
        fbAnalyticsHelper.logEvent(event,event,"app_sections")
    }


    override fun onResume() {
        super.onResume()

        if (!isDataLoaded) {
            val  cat = arguments?.getParcelable<CatResp>(CategoryFrag.KEY_CATEGORY_DETAIL)
            viewModel.getPost(cat?.id)

            isDataLoaded = true
        }
    }


    override fun openYoutube(post: Post) {


        ( activity as MvvmActivity<*,*>).gotoActivity(YtDetailActivity::class, mapOf(AppBundleKey.YOUTUBE_URL to post))
    }
}
