package com.ltthuc.habit.ui.activity.setting

import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ltthuc.habit.data.AppDataManager
import com.ltthuc.habit.ui.widget.ClickObserVable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<SettingNav, String>(schedulerProvider, connectionManager) {


      val switch = ClickObserVable{
          appDataManager.appPreferenceHelper?.enableNotification = it
      }



    fun openPolicy(){
        navigator?.gotoPolicy("")

    }

}