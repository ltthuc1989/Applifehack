package com.applifehack.knowledge.ui.activity.setting

import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.ui.widget.ClickObserVable
import javax.inject.Inject

class SettingVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<SettingNav, String>(schedulerProvider, connectionManager) {

    private val PRIVACY_URL = "https://applifehack.com/privacy.html"
    private val SUPPORT_EMAIL = "support@applifehack.com"
    val switch = ClickObserVable {

        if (it) {
            subcribePush()
        } else {
            unSubcribePush()
        }
    }


    fun openPolicy() {
        navigator?.gotoPolicy(PRIVACY_URL)

    }


    fun feedbackClick() {

        navigator?.sendFeedBack(SUPPORT_EMAIL)

    }

    fun rateUs() {
        navigator?.rateUs()
    }

    fun subcribePush() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { it1 ->

            FirebaseMessaging.getInstance().subscribeToTopic("all").addOnSuccessListener {
                appDataManager.appPreferenceHelper?.enableNotification = true
            }
        }
    }
    fun unSubcribePush(){
        FirebaseMessaging.getInstance().unsubscribeFromTopic("all").addOnSuccessListener {
            appDataManager.appPreferenceHelper?.enableNotification = false
        }
    }
}