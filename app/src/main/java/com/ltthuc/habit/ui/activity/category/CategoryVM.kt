package com.ltthuc.habit.ui.activity.category

import android.content.Context
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.ltthuc.habit.R
import com.ltthuc.habit.data.AppDataManager
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.data.network.response.CatResp
import com.ltthuc.habit.data.network.response.RssCatResp
import com.ltthuc.habit.ui.activity.HomeActivityNav
import java.util.ArrayList
import javax.inject.Inject

class CategoryVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<CategoryNav, String>(schedulerProvider, connectionManager) {

    val results = NonNullLiveData<List<CatResp>>(emptyList())
    private val mData = ArrayList<CatResp>()
    val banners = NonNullLiveData<List<Post>>(emptyList())
    private var dotsCount: Int = 0
    private var dots: Array<ImageView?>? = null


    override fun updateModel(data: String?) {
        getRssCat()
        getPopularPost()
    }
    fun getRssCat() {
        navigator?.showProgress()
        compositeDisposable.add(appDataManager.getCatgories().compose(schedulerProvider?.ioToMainSingleScheduler())
                .map {
                    it.value().toObjects(CatResp::class.java)
                }.subscribe({

                    mData.addAll(it)
                    results.value = mData

                    navigator?.hideProgress()


                }, {
                    navigator?.hideProgress()
                    navigator?.showAlert(it.message)
                }))

    }

    fun onItemClicked(item: CatResp){
        navigator?.gotoCatDetailScreen(item)
    }

    fun postPopularClick(post: Post){
        navigator?.gotoPostDetail(post)
    }

    private fun getPopularPost() {
        navigator?.showProgress()
        compositeDisposable.add(appDataManager!!.getPost().map {
            it.value().toObjects(Post::class.java)
        }
                .subscribe({
                    banners.value = it


                }, {

                    navigator?.hideProgress()
                    navigator?.showAlert(it.message)


                }))

    }

    fun getSlidePostItem(position: Int): LiveData<Post> {
        return NonNullLiveData<Post>(banners.value[position])

    }

    fun clearDot() {
        if (dots != null) {
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
                params.setMargins(4, 0, 4, 10)

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
                params.setMargins(4, 0, 4, 10)
                view.addView(it, params)
            }
        }
    }

    fun changeDot(position: Int, context: Context?) {
        for (i in 0 until dotsCount) {
            dots!![i]?.setImageDrawable(context?.resources?.getDrawable(R.drawable.nonselecteditem_dot))
        }

        dots!![position]?.setImageDrawable(context?.resources?.getDrawable(R.drawable.selecteditem_dot))
    }


}