package com.applifehack.knowledge.ui.admin.mirgrate

import androidx.appcompat.widget.Toolbar
import com.applifehack.knowledge.databinding.ActivityTransferBinding
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.ui.base.MvvmNav

class MirgrateActivity : MvvmActivity<ActivityTransferBinding,MirgrateVM>(),MvvmNav {

    override val layoutId: Int = com.applifehack.knowledge.R.layout.activity_transfer
    override val viewModel: MirgrateVM by getLazyViewModel()

    override fun onViewInitialized(binding: ActivityTransferBinding) {
        super.onViewInitialized(binding)
        viewModel.navigator = this
        binding.viewModel = viewModel
        setToolBar(binding.settingToolbar)
    }

    private fun setToolBar(toolbar: Toolbar, title: String?="") {
        toolbar.setTitle(title)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}