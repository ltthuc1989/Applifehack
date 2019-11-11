package com.ltthuc.habit.ui.activity.listpost

import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.ui.base.adapter.SingleLayoutAdapter
import com.ezyplanet.core.util.extension.getExtra
import com.ezyplanet.core.util.extension.getExtraParcel
import com.ezyplanet.core.util.extension.observe
import com.google.firebase.iid.FirebaseInstanceId
import com.ltthuc.habit.R
import com.ltthuc.habit.data.network.response.RssCatResp
import com.ltthuc.habit.databinding.ActivityHomeBinding
import com.ltthuc.habit.databinding.ActivityListPostBinding
import com.ltthuc.habit.databinding.ItemListPostBinding
import com.ltthuc.habit.databinding.ItemRssLinkBinding
import com.ltthuc.habit.ui.activity.HomeActivityNav
import com.ltthuc.habit.ui.activity.HomeActivityVM
import kotlinx.android.synthetic.main.activity_list_post.*

class ListPostActivity : MvvmActivity<ActivityListPostBinding, ListPostVM>(), ListPostNav {

    override val viewModel: ListPostVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_list_post

    companion object {
        const val KEY_FEED_URL = "FEED_URL"
    }

    override fun onViewInitialized(binding: ActivityListPostBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this

       viewModel.setModel(getExtraParcel(KEY_FEED_URL)!!)

        binding.adapter = SingleLayoutAdapter<PostContent, ItemListPostBinding>(
                R.layout.item_list_post,
                emptyList(),
                viewModel
        )

        observe(viewModel.results) {
            binding.adapter?.swapItems(it)
        }
    }

    override fun resetState() {
        recyclerView.scrollToPosition(0)
    }
}