package com.ezyplanet.core.viewmodel

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ezyplanet.core.util.livedata.ColdEventRx



open class DataChangeVM: ViewModel(){

    val isLogined= MutableLiveData<ColdEventRx<Boolean>>()
    val isNeedReload= MutableLiveData<ColdEventRx<Boolean>>()
    val locationUpdate= MutableLiveData<ColdEventRx<Location>>()



}