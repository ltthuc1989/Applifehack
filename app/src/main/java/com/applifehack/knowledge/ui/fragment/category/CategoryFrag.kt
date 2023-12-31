package com.applifehack.knowledge.ui.fragment.category

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.ezyplanet.core.ui.base.ViewModelScope
import com.ezyplanet.core.ui.base.adapter.SingleLayoutAdapter
import com.ezyplanet.core.util.extension.observe
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.network.response.CatResp
import com.applifehack.knowledge.databinding.FragmentCategoryBinding
import com.applifehack.knowledge.databinding.ItemCategoryBinding
import com.applifehack.knowledge.ui.activity.BaseActivity
import com.applifehack.knowledge.ui.activity.dynamiclink.DynamicLinkActivity
import com.applifehack.knowledge.ui.activity.home.HomeEventModel
import com.applifehack.knowledge.ui.activity.quotes.QuotesActivity
import com.applifehack.knowledge.ui.activity.ytDetail.YtDetailActivity
import com.applifehack.knowledge.ui.fragment.BaseFragment
import com.applifehack.knowledge.ui.fragment.slidepost.SlidePostFrag
import com.applifehack.knowledge.util.AppBundleKey
import java.util.*

class CategoryFrag : BaseFragment<FragmentCategoryBinding, CategoryVM>(), CategoryNav,
        ViewPager.OnPageChangeListener {

    override val viewModel: CategoryVM by getLazyViewModel(ViewModelScope.FRAGMENT)
    override val layoutId: Int = R.layout.fragment_category


    private lateinit var viewPager: ViewPager
    private var dots: Array<ImageView>? = null
    private var timer: Timer? = null
    private var count: Int = 0
    private var banners: List<Post>? = null
    lateinit var categoryShareEvent: CategoryShareEvent
     var isFirstCreated = false

    companion object {
        val KEY_CATEGORY_DETAIL = "category_detail"
    }

    lateinit var homeEventModel: HomeEventModel

    override fun setUpNavigator() {
        viewModel.navigator = this
    }

    override fun onViewInitialized(binding: FragmentCategoryBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this

        homeEventModel = ViewModelProvider(requireActivity()).get(HomeEventModel::class.java)
        categoryShareEvent = ViewModelProvider(requireActivity()).get(CategoryShareEvent::class.java)
        categoryShareEvent.catClickEvent.observe(this, androidx.lifecycle.Observer {
            viewModel.catDetailClick(it)
        })
        categoryShareEvent.postClickEvent.observe(this, androidx.lifecycle.Observer {
            viewModel.postPopularClick(it)
        })
        if(!isFirstCreated) {
            viewModel.updateModel(null)
            isFirstCreated = true
        }else{
            autoSlide()
        }




        binding.adapter = SingleLayoutAdapter<CatResp, ItemCategoryBinding>(
                R.layout.item_category,
                emptyList(),
                viewModel
        )

        viewPager = binding.viewPager
        observe(viewModel.banners) {

            if (it != null && !it.isEmpty()) { // Avoid unnecessary recreation.
                viewPager.adapter = SlidePostAdapter(childFragmentManager!!, it)
                this.banners = it
                viewModel.setUiPageViewController(binding.viewPagerCountDots, context)
                if (timer == null) {
                    autoSlide()
                }
                viewPager.setOnPageChangeListener(this)
            }
        }

        observe(viewModel.results) {
            binding.adapter?.swapItems(it!!)
        }
        val event = "explore_article"
        fbAnalyticsHelper.logEvent(event,event,"app_sections")

    }


    override fun gotoCatDetailScreen(resp: CatResp) {
       if(resp?.cat_type != null && (resp?.cat_post_type =="article")) {

            homeEventModel.categoryClick?.postValue(resp)

        } else{

            gotoActivity(QuotesActivity::class, mapOf(QuotesActivity.KEY_CAT_TYPE to resp.cat_type,QuotesActivity.KEY_CAT_NAME to resp.cat_name))
        }

    }

    override fun gotoPostDetail(post: Post) {
        if (post.getPostType() == PostType.ARTICLE) {
            (activity as BaseActivity<*, *>).openBrowser(post.redirect_link)
        } else if (post?.getPostType() == PostType.VIDEO) {
            openYoutube(post)
        } else {

            gotoActivity(DynamicLinkActivity::class, mapOf(AppBundleKey.KEY_POST_ID to post.id))
        }
    }

    override fun openYoutube(post:Post) {
        gotoActivity(YtDetailActivity::class, mapOf(AppBundleKey.YOUTUBE_URL to post))

    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPageSelected(position: Int) {
        viewModel.changeDot(position, activity)
    }


    inner class SlidePostAdapter(fm: FragmentManager, private val data: List<Post>) :
            FragmentPagerAdapter(fm) {

        override fun getCount() = data.size

        override fun getItem(position: Int): Fragment {
            return SlidePostFrag().newInstance(data[position])
        }


    }

    private fun autoSlide() {
        if (banners == null)
            return
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                activity!!.runOnUiThread(Runnable {
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

    override fun onDestroyView() {
        super.onDestroyView()
        viewPager.setOnPageChangeListener(null)
        viewModel.clearDot()
        if (timer != null) {
            timer?.cancel()
        }
    }




}