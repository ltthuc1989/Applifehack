package com.applifehack.knowledge.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.network.response.CatResp
import com.applifehack.knowledge.ui.activity.quotes.QuotesVM
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.ui.base.MvvmNav
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import javax.inject.Inject

open class BaseBottomVM<T, U> @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
):BaseViewModel<MvvmNav,Any>(schedulerProvider, connectionManager){
    val homeSelected = MutableLiveData<Boolean>()

    val catSelected  = MutableLiveData<Boolean>()
    val settingSelected = MutableLiveData<Boolean>()

    open fun onLoadMore(page: Int) {

    }
    fun getCatMD():LiveData<CatResp>{
        return  (model as LiveData<CatResp>)
    }
    fun getQuoteVM():QuotesVM{
        return  (this as QuotesVM)
    }
}