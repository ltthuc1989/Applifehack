package com.applifehack.knowledge.ui.activity.ytDetail

import androidx.lifecycle.MutableLiveData
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.applifehack.knowledge.data.AppDataManager
import javax.inject.Inject

class YtDetailVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<YtDetailNav, YtModel>(schedulerProvider, connectionManager) {

   val youtubeUrl =MutableLiveData<String>()

    fun getYtDetail(youtubeId:String?){

        apiSingleException(appDataManager.getYtDetail(youtubeId),true,{
              if(it.items?.isEmpty()!=true) {
                  val temp=YtModel(youtubeId, it.items[0])
                  updateModel(temp)
              }

        },{

            it?.message?.let {
                navigator?.showAlert(it)
            }
        })





    }

    fun playClick(){
        navigator?.openYoutube(_model?.value?.id)
    }




}