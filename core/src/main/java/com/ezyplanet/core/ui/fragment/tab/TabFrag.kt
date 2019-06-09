package com.ezyplanet.thousandhands.shipper.ui.fragment.tab

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ezyplanet.core.R
import com.ezyplanet.core.databinding.FragmentMvmTabBinding
import com.ezyplanet.core.ui.base.MvvmFragment
import com.ezyplanet.core.ui.base.MvvmNav
import com.ezyplanet.core.ui.base.ViewModelScope
import com.ezyplanet.core.ui.fragment.tab.TabVM





abstract class TabFrag : MvvmFragment<TabVM, FragmentMvmTabBinding>(), MvvmNav {

    override val viewModel: TabVM by getLazyViewModel(ViewModelScope.ACTIVITY)
    override val layoutId: Int = R.layout.fragment_mvm_tab

    override fun setUpNavigator() {
        viewModel.navigator = this
    }

    override fun onViewInitialized(binding: FragmentMvmTabBinding) {
        super.onViewInitialized(binding)

        binding.run {
            binding.viewModel = viewModel
            viewPager.offscreenPageLimit = 1
            viewPager.adapter = TabAdapter(childFragmentManager)
            tabs.setupWithViewPager(viewPager)
        }

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

    }
    abstract  fun <FRAGMENT : Fragment> FRAGMENT.getItemFragment(position:Int):Fragment
    abstract fun getTabTitle():Array<Int>
}