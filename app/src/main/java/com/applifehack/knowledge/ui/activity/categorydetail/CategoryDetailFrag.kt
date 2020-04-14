package com.applifehack.knowledge.ui.activity.categorydetail

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ezyplanet.core.ui.base.MvvmActivity
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.data.network.response.CatResp

import com.applifehack.knowledge.databinding.FragmentCategoryDetailBinding
import com.applifehack.knowledge.ui.activity.BaseActivity
import com.applifehack.knowledge.ui.activity.home.HomeActivity
import com.applifehack.knowledge.ui.activity.setting.SettingActivity
import com.applifehack.knowledge.ui.fragment.category.CategoryFrag
import com.applifehack.knowledge.ui.fragment.articlelist.ArticleListFrag
import com.applifehack.knowledge.ui.fragment.videolist.VideoListFrag
import com.ezyplanet.core.util.extension.gotoActivity
import com.ezyplanet.core.util.extension.gotoActivityClearTask
import kotlinx.android.synthetic.main.view_category_detail.*
import kotlinx.android.synthetic.main.view_category_detail.view.*
import javax.inject.Inject

class CategoryDetailFrag : BaseActivity<FragmentCategoryDetailBinding, CategoryDetailVM>(),
        CategoryDetailNav {

    override val viewModel: CategoryDetailVM by getLazyViewModel()
    override val layoutId: Int = R.layout.fragment_category_detail
    lateinit var cat:CatResp
    @Inject lateinit var fbAnalyticsHelper: FirebaseAnalyticsHelper

    override fun onViewInitialized(binding: FragmentCategoryDetailBinding) {
        super.onViewInitialized(binding)
        viewModel.navigator = this
        setToolBar(binding.tagFeedLayout.view_toolbar, "")
        binding.viewModel = viewModel
        cat=intent.getParcelableExtra<CatResp>(CategoryFrag.KEY_CATEGORY_DETAIL)

        viewModel.updateModel(cat)
        binding.listener = this

        binding.viewPager.offscreenPageLimit = 1
        binding.viewPager.adapter = TabAdapter(supportFragmentManager)

        tabs.setupWithViewPager(binding.viewPager)
        val event = "explore_category_detail"
        fbAnalyticsHelper.logEvent(event,event,"app_sections")


    }

    inner class TabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount() = getTabTitle().size


        override fun getItem(position: Int): Fragment {
            return getItemFragment(position)
        }


        override fun getPageTitle(position: Int): CharSequence {
            return resources.getString(getTabTitle()[position]).toUpperCase()
        }

        private fun getItemFragment(position: Int): Fragment {

            return if (position == 0) ArticleListFrag.newInstance(cat)
            else return VideoListFrag.newInstance(cat)

        }

        private fun getTabTitle(): Array<Int> {
            return arrayOf(

                    R.string.article,
                    R.string.video
            )

        }

    }

    override fun onSetting() {
        gotoActivity(SettingActivity::class)
    }

    override fun onHome() {
        gotoActivity(HomeActivity::class,true)

    }

    override fun onSaved() {
    }

    override fun onCategory() {
       finish()
    }



}