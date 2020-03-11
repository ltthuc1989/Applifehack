package com.ltthuc.habit.ui.activity.categorydetail

import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.ui.base.ViewModelScope
import com.ezyplanet.core.util.extension.getExtraParcel
import com.ezyplanet.core.util.extension.putArgs
import com.ltthuc.habit.R
import com.ltthuc.habit.data.firebase.FirebaseAnalyticsHelper
import com.ltthuc.habit.data.network.response.CatResp

import com.ltthuc.habit.databinding.FragmentCategoryDetailBinding
import com.ltthuc.habit.ui.activity.BaseActivity
import com.ltthuc.habit.ui.fragment.BaseFragment
import com.ltthuc.habit.ui.fragment.category.CategoryFrag
import com.ltthuc.habit.ui.fragment.articlelist.ArticleListFrag
import com.ltthuc.habit.ui.fragment.videolist.VideoListFrag
import com.ltthuc.habit.ui.widget.listener.ToolbarListener
import kotlinx.android.synthetic.main.view_category_detail.*
import kotlinx.android.synthetic.main.view_category_detail.view.*
import javax.inject.Inject

class CategoryDetailFrag : MvvmActivity<FragmentCategoryDetailBinding, CategoryDetailVM>(),
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


    private fun setToolBar(toolbar: Toolbar, title: String) {
        toolbar.setTitle(title)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }



}