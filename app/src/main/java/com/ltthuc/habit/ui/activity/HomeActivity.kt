package com.ltthuc.habit.ui.activity

import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.ui.base.adapter.SingleLayoutAdapter
import com.ezyplanet.core.util.extension.gotoActivity
import com.ezyplanet.core.util.extension.observe
import com.google.firebase.iid.FirebaseInstanceId
import com.ltthuc.habit.R
import com.ltthuc.habit.data.network.response.RssCatResp
import com.ltthuc.habit.databinding.ActivityHomeBinding
import com.ltthuc.habit.databinding.ItemRssLinkBinding
import com.ltthuc.habit.ui.activity.listpost.ListPostActivity

class HomeActivity : MvvmActivity<ActivityHomeBinding,HomeActivityVM>(),HomeActivityNav{

    override val viewModel: HomeActivityVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_home

    override fun onViewInitialized(binding: ActivityHomeBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) { it1 ->
            viewModel.getRssCat()
        }



        binding.adapter = SingleLayoutAdapter<RssCatResp, ItemRssLinkBinding>(
                R.layout.item_rss_link,
                emptyList(),
                viewModel
        )

        observe(viewModel.results) {
            binding.adapter?.swapItems(it)
        }
    }

    override fun gotoListPostScreen(resp: RssCatResp) {
       gotoActivity(ListPostActivity::class, mapOf(ListPostActivity.KEY_FEED_URL to resp))
    }
}