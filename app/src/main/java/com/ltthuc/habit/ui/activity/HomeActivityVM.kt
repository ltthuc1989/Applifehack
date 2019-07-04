package com.ltthuc.habit.ui.activity

import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.ltthuc.habit.data.AppDataManager
import com.ltthuc.habit.data.network.response.RssCatResp
import java.util.ArrayList
import javax.inject.Inject

class HomeActivityVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<HomeActivityNav, String>(schedulerProvider, connectionManager) {

    val results = NonNullLiveData<List<RssCatResp>>(emptyList())
    private val mData = ArrayList<RssCatResp>()



    fun getRssCat() {
        navigator?.showProgress()
        compositeDisposable.add(appDataManager.getRssCat().compose(schedulerProvider?.ioToMainSingleScheduler())
                .map {
                    it.value().toObjects(RssCatResp::class.java)
                }.subscribe({

                    mData.addAll(it)
                    results.value = mData

                    navigator?.hideProgress()


                }, {
                    navigator?.hideProgress()
                }))

    }

    fun onItemClicked(item:RssCatResp){

    }


}

