package com.ltthuc.habit.ui.fragment.articlelist

import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.ltthuc.habit.data.AppDataManager
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.data.network.response.RssCatResp
import com.ltthuc.habit.ui.activity.HomeActivityNav
import java.util.ArrayList
import javax.inject.Inject

class ArticleListVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<ArticleListNav, String>(schedulerProvider, connectionManager) {

    val results = NonNullLiveData<List<Post>>(emptyList())
    private val mData = ArrayList<Post>()



    fun getRssCat(catId:String?) {
        navigator?.showProgress()
        compositeDisposable.add(appDataManager.getPostByCat(catId).compose(schedulerProvider?.ioToMainSingleScheduler())
                .map {
                    it.value().toObjects(Post::class.java)
                }.subscribe({

                    mData.addAll(it)
                    results.value = mData

                    navigator?.hideProgress()


                }, {
                    navigator?.hideProgress()
                    navigator?.showAlert(it.message)
                }))

    }

    fun onItemClicked(item: Post){
        navigator?.gotoPostDetail(item)
    }




}
