package com.ltthuc.habit.ui.activity.ytDetail

import android.provider.MediaStore
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.google.api.services.youtube.model.Video
import com.ltthuc.habit.data.AppDataManager
import com.ltthuc.habit.data.network.response.CatResp
import com.ltthuc.habit.data.network.response.youtube.Item
import com.ltthuc.habit.ui.activity.categorydetail.CategoryDetailNav
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class YtDetailVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<YtDetailNav, YtModel>(schedulerProvider, connectionManager) {



    fun getYtDetail(youtubeId:String?){

        apiSingle(appDataManager.getYtDetail(youtubeId),{

            updateModel(YtModel(youtubeId,it.items[0]))

        },true)





    }

    fun playClick(id:String){
        navigator?.openYoutube(id)
    }




}