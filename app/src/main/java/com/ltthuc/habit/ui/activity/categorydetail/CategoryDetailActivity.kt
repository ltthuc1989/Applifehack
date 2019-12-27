package com.ltthuc.habit.ui.activity.categorydetail

import androidx.core.view.GravityCompat
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
import com.ezyplanet.core.util.extension.gotoActivity
import com.ltthuc.habit.R
import com.ltthuc.habit.data.network.response.CatResp
import com.ltthuc.habit.databinding.ActivityCategoryDetailBinding
import com.ltthuc.habit.ui.activity.category.CategoryActivity
import com.ltthuc.habit.ui.activity.feed.FeedActivity
import com.ltthuc.habit.ui.fragment.articlelist.ArticleListFrag
import com.ltthuc.habit.ui.fragment.videolist.VideoListFrag
import com.ltthuc.habit.ui.widget.listener.NavListener
import com.ltthuc.habit.ui.widget.listener.ToolbarListener
import kotlinx.android.synthetic.main.view_category_detail.*

class CategoryDetailActivity : MvvmActivity<ActivityCategoryDetailBinding, CategoryDetailVM>(),
        CategoryDetailNav, NavListener,ToolbarListener {

    override val viewModel: CategoryDetailVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_category_detail
    lateinit var catId:String


    override fun onViewInitialized(binding: ActivityCategoryDetailBinding) {
        super.onViewInitialized(binding)
        viewModel.navigator = this
        binding.viewModel = viewModel
        val catResp = getExtraParcel<CatResp>(CategoryActivity.KEY_CATEGORY_DETAIL)
        catId = catResp?.id!!
        viewModel.updateModel(catResp)
        binding.homeNavigationView.setListner(this)
        binding.viewPager.offscreenPageLimit = 1
        binding.viewPager.adapter = TabAdapter(supportFragmentManager)

        tabs.setupWithViewPager(binding.viewPager)


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

            return if (position == 0) ArticleListFrag.newInstance(catId)
            else return VideoListFrag.newInstance(catId)

        }

        private fun getTabTitle(): Array<Int> {
            return arrayOf(

                    R.string.article,
                    R.string.video
            )

        }

    }

    override fun onSetting() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onHome() {
        gotoActivity(FeedActivity::class)
    }

    override fun onSaved() {
    }

    override fun onCategory() {
        gotoActivity(CategoryActivity::class)
    }

    override fun onMenu() {
        if (!isOpenDrawer || !binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            isOpenDrawer = true

        } else {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            isOpenDrawer = false
        }
    }


}