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
import com.ltthuc.habit.data.entity.PostType
import com.ltthuc.habit.data.network.response.CatResp
import com.ltthuc.habit.databinding.ActivityCategoryBinding
import com.ltthuc.habit.databinding.ItemCategoryBinding
import com.ltthuc.habit.ui.activity.categorydetail.CategoryDetailActivity
import com.ltthuc.habit.ui.activity.feed.FeedActivity
import com.ltthuc.habit.ui.activity.quotes.QuotesActivity
import com.ltthuc.habit.ui.fragment.slidepost.SlidePostFrag
import com.ltthuc.habit.ui.widget.listener.NavListener
import com.ltthuc.habit.util.CustomTabHelper
import com.ltthuc.habit.util.extension.openLink
import java.util.*

class CategoryActivity : MvvmActivity<ActivityCategoryBinding, CategoryVM>(), CategoryNav,ViewPager.OnPageChangeListener,NavListener {

    override val viewModel: CategoryVM by getLazyViewModel()
    override val layoutId: Int = R.layout.activity_category

    private var customTabHelper: CustomTabHelper = CustomTabHelper()

    private lateinit var viewPager: ViewPager
    private var dots: Array<ImageView>? = null
    private var timer: Timer? = null
    private var count: Int = 0
    private var banners: List<Post>? = null
    companion object {
        val KEY_CATEGORY_DETAIL = "category_detail"
    }

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
        binding.homeNavigationView.setListner(this)
    }



    override fun gotoCatDetailScreen(resp: CatResp) {
        gotoActivity(CategoryDetailActivity::class, mapOf(KEY_CATEGORY_DETAIL to resp))
    }

    override fun gotoPostDetail(post: Post) {
        if(post.getPostType()==PostType.ARTICLE) {
            openLink(post?.redirect_link, customTabHelper)
        }else if(post?.getPostType()==PostType.QUOTE) {
            gotoActivity(QuotesActivity::class)
        }else{
            openYoutube(post?.video_url)
        }
    }

    override fun openYoutube(link: String?) {

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

}