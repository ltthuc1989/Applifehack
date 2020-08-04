package com.applifehack.knowledge.ui.activity.home

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.util.livedata.SingleEventLiveData
import com.applifehack.knowledge.data.network.response.CatResp

class HomeEventModel : ViewModel(){
    val categoryClick =  SingleEventLiveData<CatResp>()
    val toolbarTitle = SingleEventLiveData<String>()
    val refreshClick = SingleEventLiveData<Boolean>()
    val showRefresh = SingleEventLiveData<Boolean>()

}