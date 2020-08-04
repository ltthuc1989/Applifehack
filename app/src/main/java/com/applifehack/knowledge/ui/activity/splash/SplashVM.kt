package com.applifehack.knowledge.ui.activity.splash

import android.content.Context
import com.applifehack.knowledge.R
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.firebase.PayloadResult
import com.applifehack.knowledge.util.AlertDialogUtils
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.AlertUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashVM  @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<SplashNav, String>(schedulerProvider, connectionManager) {



    fun setData(context: Context,data:PayloadResult?){
        forceCrash()
        if(isNetworkConnected()!=true){
            AlertDialogUtils.showInternetAlertDialog(context,R.string.connection_fail,R.string.please_check_your_internet,{
                setData(context,data)
            },{

                (context as MvvmActivity<*,*>).finish()
            })
            return
        }
        val appLaunchCount = appDataManager.appPreferenceHelper.appLaunchCount
        appDataManager.appPreferenceHelper.appLaunchCount=appLaunchCount?.plus(1)
        uiScope?.launch {
            delay(3*1000)
           navigator?.gotoHomeScreen(data)

        }
    }

    private fun forceCrash(){
       // throw RuntimeException("Test Crash")
    }






}