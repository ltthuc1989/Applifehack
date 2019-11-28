package com.ltthuc.habit.ui.activity.categorydetail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ezyplanet.core.databinding.FragmentMvmTabBinding
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.ui.base.MvvmFragment
import com.ezyplanet.core.ui.base.MvvmNav
import com.ezyplanet.core.ui.base.ViewModelScope
import com.ezyplanet.core.ui.fragment.tab.TabVM
import com.ezyplanet.core.util.extension.getExtraParcel
import com.ltthuc.habit.R
import com.ltthuc.habit.databinding.ActivityCategoryDetailBinding
import com.ltthuc.habit.ui.activity.category.CategoryActivity
import com.ltthuc.habit.ui.fragment.articlelist.ArticleListFrag
import com.ltthuc.habit.ui.fragment.videolist.VideoListFrag
import kotlinx.android.synthetic.main.view_category_detail.*

class CategoryDetailActivity : MvvmActivity<ActivityCategoryDetailBinding,CategoryDetailVM>(), CategoryDetailNav {

    override val viewModel: CategoryDetailVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_category_detail



    override fun onViewInitialized(binding: ActivityCategoryDetailBinding) {
        super.onViewInitialized(binding)
        viewModel.navigator = this
        binding.run {
            binding.viewModel = viewModel
            viewPager.offscreenPageLimit = 1
            viewPager.adapter = TabAdapter(supportFragmentManager)
            tabs.setupWithViewPager(viewPager)
        }
        viewModel.updateModel(getExtraParcel(CategoryActivity.KEY_CATEGORY_DETAIL))

    }

    inner class TabAdapter(fm: FragmentManager) :
            FragmentPagerAdapter(fm) {

        override fun getCount() = getTabTitle().size


        override fun getItem(position: Int): Fragment {
            return getItemFragment(position)
        }




        override fun getPageTitle(position: Int): CharSequence {
            return resources.getString(getTabTitle()[position]).toUpperCase()
        }

        private fun getItemFragment(position: Int): Fragment {

            return if(position==0) ArticleListFrag()
            else return VideoListFrag()

        }

        private fun getTabTitle(): Array<Int> {
            return arrayOf(

                    R.string.article,
                    R.string.video
            )

        }

    }


}