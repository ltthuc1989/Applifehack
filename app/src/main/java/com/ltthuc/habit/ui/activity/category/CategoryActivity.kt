package com.ltthuc.habit.ui.activity.category

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.ui.base.adapter.SingleLayoutAdapter
import com.ezyplanet.core.util.extension.gotoActivity
import com.ezyplanet.core.util.extension.observe
import com.google.firebase.iid.FirebaseInstanceId
import com.ltthuc.habit.R
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.data.network.response.CatResp
import com.ltthuc.habit.data.network.response.RssCatResp
import com.ltthuc.habit.databinding.ActivityCategoryBinding
import com.ltthuc.habit.databinding.ActivityHomeBinding
import com.ltthuc.habit.databinding.ItemCategoryBinding
import com.ltthuc.habit.databinding.ItemRssLinkBinding
import com.ltthuc.habit.ui.activity.HomeActivityNav
import com.ltthuc.habit.ui.activity.HomeActivityVM
import com.ltthuc.habit.ui.activity.listpost.ListPostActivity
import com.ltthuc.habit.ui.fragment.slidepost.SlidePostFrag
import com.ltthuc.habit.util.CustomTabHelper
import com.ltthuc.habit.util.extension.gotoPostDetail
import java.util.*

class CategoryActivity : MvvmActivity<ActivityCategoryBinding, CategoryVM>(), CategoryNav,ViewPager.OnPageChangeListener {

    override val viewModel: CategoryVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_category

    private var customTabHelper: CustomTabHelper = CustomTabHelper()

    private lateinit var viewPager: ViewPager
    private var dots: Array<ImageView>? = null
    private var timer: Timer? = null
    private var count: Int = 0
    private var banners: List<Post>? = null

    override fun onViewInitialized(binding: ActivityCategoryBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) { it1 ->
            viewModel.updateModel(null)

        }



        binding.adapter = SingleLayoutAdapter<CatResp, ItemCategoryBinding>(
                R.layout.item_category,
                emptyList(),
                viewModel
        )

        viewPager= binding.viewPager
        observe(viewModel.banners){

            if (it != banners&&!it.isEmpty()) { // Avoid unnecessary recreation.
                viewPager.adapter = SlidePostAdapter(supportFragmentManager, it)
                banners = it
                viewModel.setUiPageViewController(binding.viewPagerCountDots,this)
                if (timer == null) {
                    autoSlide()
                }
                viewPager.setOnPageChangeListener(this)
            }
        }

        observe(viewModel.results) {
            binding.adapter?.swapItems(it)
        }
    }



    override fun gotoCatDetailScreen(resp: CatResp) {
    }

    override fun gotoPostDetail(post: Post) {
        gotoPostDetail(post,customTabHelper)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPageSelected(position: Int) {
        viewModel.changeDot(position,this)
    }


    inner class SlidePostAdapter(fm: FragmentManager, private val data: List<Post>) :
            FragmentPagerAdapter(fm) {

        override fun getCount() = data.size

        override fun getItem(position: Int): Fragment {
            return  SlidePostFrag().newInstance(position)
        }



    }

    private fun autoSlide() {
        if (banners == null)
            return
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread(Runnable {
                    if (viewPager == null || banners == null)
                        return@Runnable
                    if (count <= banners!!.size) {
                        viewPager.setCurrentItem(count)
                        count++
                    } else {
                        count = 0
                        viewPager.setCurrentItem(count)
                    }
                })
            }
        }, 500, 3000)

    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager.setOnPageChangeListener(null)
        viewModel.clearDot()
        if (timer != null) {
            timer?.cancel()
        }
    }
}