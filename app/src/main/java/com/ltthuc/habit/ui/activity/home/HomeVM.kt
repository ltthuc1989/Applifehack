package com.ltthuc.habit.ui.activity.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ltthuc.habit.R
import com.ltthuc.habit.data.AppDataManager
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.util.RateUs

import javax.inject.Inject


class HomeVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<HomeNav, String>(schedulerProvider, connectionManager) {


    fun getVersionCode(): Int? = appDataManager?.appPreferenceHelper?.versionUpdate

    fun showRateUse(activity: HomeActivity){


        if(appDataManager.appPreferenceHelper.appLaunchCount==4){
            RateUs.rate(activity,appDataManager.appPreferenceHelper)
        }
    }

}