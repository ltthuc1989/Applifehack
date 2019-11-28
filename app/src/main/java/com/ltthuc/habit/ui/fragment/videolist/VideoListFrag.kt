package com.ltthuc.habit.ui.fragment.videolist

import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.ui.base.MvvmFragment
import com.ezyplanet.core.ui.base.ViewModelScope
import com.ezyplanet.core.ui.base.adapter.SingleLayoutAdapter
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.core.util.extension.observe
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.google.firebase.iid.FirebaseInstanceId
import com.ltthuc.habit.R
import com.ltthuc.habit.data.AppDataManager
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.data.network.response.RssCatResp
import com.ltthuc.habit.databinding.FragArticleListBinding
import com.ltthuc.habit.databinding.FragVideoListBinding
import com.ltthuc.habit.databinding.ItemCatTopicBinding
import com.ltthuc.habit.databinding.ItemCatVideoBinding
import com.ltthuc.habit.ui.activity.HomeActivityNav
import com.ltthuc.habit.ui.fragment.articlelist.ArticleListNav
import com.ltthuc.habit.ui.fragment.articlelist.ArticleListVM
import java.util.ArrayList
import javax.inject.Inject

class VideoListFrag : MvvmFragment<VideoListVM, FragVideoListBinding>(), VideoListNav {

    override val viewModel: VideoListVM by getLazyViewModel(ViewModelScope.FRAGMENT)
    override val layoutId: Int = R.layout.frag_video_list
    override fun setUpNavigator() {
        viewModel.navigator = this
    }
    override fun onViewInitialized(binding: FragVideoListBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel


        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(activity!!) { it1 ->
            viewModel.getPosts()
        }



        binding.adapter = SingleLayoutAdapter<Post, ItemCatVideoBinding>(
                R.layout.item_cat_video,
                emptyList(),
                viewModel
        )

        observe(viewModel.results) {
            binding.adapter?.swapItems(it)
        }
    }





    override fun openYoutube(link: String?) {
    }
}
