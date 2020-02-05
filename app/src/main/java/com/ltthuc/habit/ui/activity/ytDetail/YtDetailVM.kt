package com.ltthuc.habit.ui.activity.ytDetail

import androidx.lifecycle.MutableLiveData
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ltthuc.habit.data.AppDataManager
import javax.inject.Inject

class YtDetailVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<YtDetailNav, YtModel>(schedulerProvider, connectionManager) {

   val youtubeUrl =MutableLiveData<String>()

    fun getYtDetail(youtubeId:String?){

        apiSingle(appDataManager.getYtDetail(youtubeId),{

            updateModel(YtModel(youtubeId,it.items[0]))

        },true)





    }

    fun playClick(id:String){
        navigator?.openYoutube(id)
    }




}