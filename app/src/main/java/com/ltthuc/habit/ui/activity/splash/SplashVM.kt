package com.ltthuc.habit.ui.activity.splash

import android.content.Intent
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.core.util.extension.getExtraParcel
import com.ezyplanet.core.util.extension.gotoActivity
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ltthuc.habit.data.AppDataManager
import com.ltthuc.habit.data.firebase.PayloadResult
import com.ltthuc.habit.ui.activity.home.HomeActivity
import com.ltthuc.habit.util.AppBundleKey
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashVM  @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<SplashNav, String>(schedulerProvider, connectionManager) {



    fun setData(data:PayloadResult?){
        val appLaunchCount = appDataManager.appPreferenceHelper.appLaunchCount
        appDataManager.appPreferenceHelper.appLaunchCount=appLaunchCount?.plus(1)
        uiScope?.launch {
            delay(3*1000)
           navigator?.gotoHomeScreen(data)

        }
    }






}