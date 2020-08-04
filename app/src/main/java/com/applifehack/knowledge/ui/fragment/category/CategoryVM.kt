package com.applifehack.knowledge.ui.fragment.category

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.data.network.response.CatResp
import com.applifehack.knowledge.util.extension.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit.http.POST
import java.util.ArrayList
import javax.inject.Inject

class CategoryVM @Inject constructor(
    val appDataManager: AppDataManager,
    schedulerProvider: SchedulerProvider,
    connectionManager: BaseConnectionManager
) : BaseViewModel<CategoryNav, String>(schedulerProvider, connectionManager) {

    val results = NonNullLiveData<List<CatResp>>(emptyList())
    private val mData = ArrayList<CatResp>()
    val banners = NonNullLiveData<List<Post>>(emptyList())
    private val mDataBanner = ArrayList<Post>()
    private var dotsCount: Int = 0
    private var dots: Array<ImageView?>? = null

    @Inject
    lateinit var fbAnalyticsHelper: FirebaseAnalyticsHelper


    override fun updateModel(data: String?) {
        getRssCat()
        getPopularPost()
    }

    fun getRssCat() {
        navigator?.showProgress()
        uiScope.launch {
            val data = appDataManager.getCatgories()
            try {
                data?.await().let {
                    if (!it.isEmpty) {
                        val snapshot = async(Dispatchers.Default) {
                            it.toObjects(CatResp::class.java)
                        }

                        mData.addAll(snapshot.await())
                        results.value = mData
                    }
                    navigator?.hideProgress()

                }
            }catch (ex:Exception){
                navigator?.hideProgress()
            }
        }


    }

    fun onItemClicked(item: CatResp) {
        val event = "category_${item.cat_name}"
        fbAnalyticsHelper.logEvent(event, event, "app_sections")
        fbAnalyticsHelper.logEvent("category_appview", "category_appview", "app_attribute")
        navigator?.gotoCatDetailScreen(item)
        Log.d("onItemClicked","cat click")


    }

    fun postPopularClick(post: Post) {
        navigator?.gotoPostDetail(post)
        val event = "popular_${post.id}"
        fbAnalyticsHelper.logEvent(event, event, "app_sections")
        fbAnalyticsHelper.logEvent("popular_appview", "popular_appview", "app_attribute")
    }

    fun catDetailClick(catId: String) {
        navigator?.gotoCatDetailScreen(getCat(catId))
        val event = "popular_cat_${catId}"
        fbAnalyticsHelper.logEvent(event, event, "app_sections")
        fbAnalyticsHelper.logEvent("popular_cat_appview", "popular_cat_appview", "app_attribute")
    }

    private fun getPopularPost() {
        navigator?.showProgress()
        uiScope?.launch {
            try {
                val data = appDataManager.getPopularPost().addOnFailureListener {
                    Log.d("abc", "abc")
                }


                data?.await().let {
                    if (!it.isEmpty) {
                        val snapshot = async(Dispatchers.Default) {
                            it.toObjects(Post::class.java)
                        }

                        val rs = snapshot.await()

                        mDataBanner.addAll(async(Dispatchers.Default) {
                            sortByViewCount(rs)
                        }.await())

                        banners.value = mDataBanner


                    }

                }

            } catch (ex: Exception) {
                navigator?.hideProgress()

            }

        }


    }

    fun getSlidePostItem(position: Int): LiveData<Post> {
        return NonNullLiveData<Post>(banners.value[position])

    }

    fun clearDot() {
        if (dots != null && dots!![0] != null) {
            (dots!![0]?.parent as LinearLayout).removeAllViews()

        }
    }

    fun setUiPageViewController(view: LinearLayout, context: Context?) {
        dotsCount = banners.value.size
        if (dots == null) {
            dots = arrayOfNulls<ImageView>(dotsCount)


            if (dotsCount <= 1) {
                return
            }


            for (i in 0 until dotsCount) {
                dots!![i] = ImageView(context)
                dots!![i]?.setImageDrawable(context?.resources?.getDrawable(R.drawable.nonselecteditem_dot))

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )

                params.gravity = Gravity.BOTTOM
                params.setMargins(30, 0, 30, 10)

                view.addView(dots!![i], params)
            }
            if (dots != null && dots!!.size > 0) {
                dots!![0]?.setImageDrawable(context?.resources?.getDrawable(R.drawable.selecteditem_dot))
            }
        } else {

            dots!!.forEach {
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )

                params.gravity = Gravity.BOTTOM
                params.setMargins(30, 0, 30, 10)
                it?.let {
                    view.addView(it, params)
                }
            }
        }
    }

    fun changeDot(position: Int, context: Context?) {
        for (i in 0 until dotsCount) {
            dots!![i]?.setImageDrawable(context?.resources?.getDrawable(R.drawable.nonselecteditem_dot))
        }

        dots!![position]?.setImageDrawable(context?.resources?.getDrawable(R.drawable.selecteditem_dot))
    }

    private fun getCat(catId: String): CatResp {
        mData.forEach {
            if (it.cat_id == catId) {
                return it
            }
        }
        return CatResp()
    }

    private suspend fun sortByViewCount(list: List<Post>): List<Post> {
        val temp = list.sortedByDescending {it.viewsCount}
        if(temp.size>5){
            return temp.subList(0,5)
        }
        return list.sortedByDescending {it.viewsCount}
    }


}