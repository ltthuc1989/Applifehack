package com.ltthuc.habit.ui.fragment.videolist

import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.ltthuc.habit.data.AppDataManager
import com.ltthuc.habit.data.entity.Post
import javax.inject.Inject


class VideoListVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<VideoListNav, String>(schedulerProvider, connectionManager) {

    val results = NonNullLiveData<List<Post>>(emptyList())
    private val mData = ArrayList<Post>()



    fun getPosts(catId:String?) {
        navigator?.showProgress()
        compositeDisposable.add(appDataManager.getVideoPostByCat(catId).compose(schedulerProvider?.ioToMainSingleScheduler())
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
        navigator?.openYoutube(item.url)
    }




}
