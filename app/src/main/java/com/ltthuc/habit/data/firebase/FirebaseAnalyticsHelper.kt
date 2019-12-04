package com.ltthuc.habit.data.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class FirebaseAnalyticsHelper @Inject constructor() {
    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics


    fun log(eventType: AnalyticsEventType?, value: String) {

        val bundle = Bundle()
       // bundle.putString(eventType?.event + extraInfo, valueType?.name + " " + extraInfo)
       // firebaseAnalytics.logEvent(eventType?.event!! + extraInfo, bundle)
    }


}

enum class AnalyticsEventType(val event: String?) {
    VIEW_POST("")



}

enum class AnalyticsValueType(val value: String?) {

}