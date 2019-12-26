package com.ltthuc.habit.ui.activity.splash

import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ltthuc.habit.data.AppDataManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashVM  @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<SplashNav, String>(schedulerProvider, connectionManager) {


    override fun updateModel(data: String?) {
        uiScope?.launch {
            delay(3*1000)
            navigator?.gotoHomeScreen()
        }
    }

}