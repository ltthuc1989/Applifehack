package com.applifehack.knowledge.ui.admin.manualpost

import com.ezyplanet.core.ui.base.MvvmActivity
import com.applifehack.knowledge.R
import com.applifehack.knowledge.databinding.ActivityHomeBinding
import com.applifehack.knowledge.databinding.ActivityManualPostBinding
import com.applifehack.knowledge.databinding.ActivtyRssBinding

class ManualPostActivity : MvvmActivity<ActivityManualPostBinding, ManualPostVM>(), ManualPostNav {

    override val viewModel: ManualPostVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_manual_post

    override fun onViewInitialized(binding: ActivityManualPostBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this
        viewModel.initData(this)
    }



}