package com.ezyplanet.core.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ezyplanet.thousandhands.util.livedata.HotEventRx

class DialogEventVM :ViewModel(){
    val suceess = MutableLiveData<HotEventRx<Any>>()
}