package com.ltthuc.habit.data.firebase

import android.os.Bundle
import androidx.annotation.WorkerThread
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAnalyticsHelper @Inject constructor(val connectionManager: BaseConnectionManager){

   @Inject lateinit var firebaseAnalytics: FirebaseAnalytics

    fun logEvent(str: String, str2: String, str3: String): Boolean {
        if (connectionManager.isNetworkConnected()==true) {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, str)
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, str2)
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, str3)
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        }
        return true
    }


}

object ParamItemName {
    val OPEN_APP_COUNT="open_app_count"

}
object ParamContentType{
    val OPEN_APP_BY_USER = "Open_app_by_user"
}