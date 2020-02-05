package com.ltthuc.habit.ui.activity.home

import androidx.lifecycle.ViewModel
import com.ezyplanet.core.util.livedata.SingleEventLiveData
import com.ltthuc.habit.data.network.response.CatResp

class HomeEventModel : ViewModel(){
    val categoryClick =  SingleEventLiveData<CatResp>()
}