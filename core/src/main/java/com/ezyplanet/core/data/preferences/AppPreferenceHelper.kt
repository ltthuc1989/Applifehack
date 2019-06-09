package com.ezyplanet.thousandhands.shipper.data.preferences

import android.content.Context
import java.util.*
import javax.inject.Inject

/**
 * Created by jyotidubey on 04/01/18.
 */
 class AppPreferenceHelper @Inject constructor(context: Context){
    companion object {
        const val PREF_NAME = "pref_arch"
        const val PREF_KEY_USER="PREF_KEY_USER"
        const val PREF_KEY_SETTINGS="PREF_KEY_SETTINGS"
        const val PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE"
        const val PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID"
        const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
        const val PREF_KEY_CURRENT_USER_NAME = "PREF_KEY_CURRENT_USER_NAME"
        const val PREF_KEY_CURRENT_USER_EMAIL = "PREF_KEY_CURRENT_USER_EMAIL"
        const val PREF_KEY_CURRENT_USER_PASSWORD = "PREF_KEY_CURRENT_USER_PASSWORD"
        const val PREF_KEY_SOCKET_CLIENT= "PREF_KEY_socket_client"
        const val PREF_KEY_TRIP_INFO= "PREF_KEY_trip_info"
        const val PREF_KEY_TOPIC_NAME= "PREF_KEY_topic_token"
        const val PREF_KEY_LANGUAGE= "PREF_KEY_language"
        const val PREF_KEY_TRIP_FINISHED= "PREF_KEY_finished_trip"
        const val PREF_KEY_IS_TRAFFICE_ENABLE= "PREF_KEY_isTrafficEnable"
        const val PREF_KEY_SHOW_UPDATE_AGAIN = "show_update_again"
        const val PREF_KEY_FORCE_UPDATE = "FORCE_UPDATE"
        const val PREF_KEY_FIRST_LOGIN = "FIRST_LOGIN"
        const val PREF_KEY_COUNTRY_CODE = "COUNTRY_CODE"
        val PREF_KEY_USER_LANGUAGE= "user_language"
        val PREF_KEY_ADS = "ads_local"
        val PREF_KEY_REFER_BY = "refer_by"
        val PREF_KEY_AGREE_PRIVACY = "AGREE_PRIVACY"
        val PREF_KEY_LOCATION_HISTORY = "location_history"
        val PREF_KEY_DATE = "show_take_order"

    }

    val mPrefs by lazy { context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) }

    var token: String? by PreferenceDelegate(mPrefs, PREF_KEY_ACCESS_TOKEN, "")
    var loginMode :Int by PreferenceDelegate(mPrefs, PREF_KEY_USER_LOGGED_IN_MODE,0)
    var user :String? by  PreferenceDelegate(mPrefs, PREF_KEY_USER,"")
    var settings :String? by  PreferenceDelegate(mPrefs, PREF_KEY_SETTINGS,"")
    var socketClient :String? by  PreferenceDelegate(mPrefs, PREF_KEY_SOCKET_CLIENT,"")
    var currentTrip :String? by PreferenceDelegate(mPrefs, PREF_KEY_TRIP_INFO,"")
    var topic_token :String? by PreferenceDelegate(mPrefs, PREF_KEY_TOPIC_NAME,"")
    var language :Boolean by PreferenceDelegate(mPrefs, PREF_KEY_LANGUAGE,false)
    var finishedTrip :Boolean by PreferenceDelegate(mPrefs, PREF_KEY_TRIP_FINISHED,false)
    var isTrafficEnable :Boolean by PreferenceDelegate(mPrefs, PREF_KEY_IS_TRAFFICE_ENABLE,false)
    var email: String? by PreferenceDelegate(mPrefs, PREF_KEY_CURRENT_USER_EMAIL,"")
    var password: String? by PreferenceDelegate(mPrefs, PREF_KEY_CURRENT_USER_PASSWORD,"")
    var showUpdateAgain :Boolean by PreferenceDelegate(mPrefs, PREF_KEY_SHOW_UPDATE_AGAIN,false)
    var forceUpdate :Boolean by PreferenceDelegate(mPrefs, PREF_KEY_FORCE_UPDATE,false)
    var isFirstLogin :Boolean by PreferenceDelegate(mPrefs, PREF_KEY_FIRST_LOGIN,true)
    var countryCode :String? by PreferenceDelegate(mPrefs, PREF_KEY_COUNTRY_CODE,"")
    var userLanguage:String? by PreferenceDelegate(mPrefs, PREF_KEY_USER_LANGUAGE, if(Locale.getDefault().language == "en"){"en"}else{"zh-hant"})
    var ads:String? by PreferenceDelegate(mPrefs, PREF_KEY_ADS,"")
    var referBy:String? by PreferenceDelegate(mPrefs, PREF_KEY_REFER_BY,"")
    var agreePrivacy :Boolean by PreferenceDelegate(mPrefs, PREF_KEY_AGREE_PRIVACY,false)
    var location_history :String? by PreferenceDelegate(mPrefs, PREF_KEY_LOCATION_HISTORY,"")
    var date :String? by PreferenceDelegate(mPrefs, PREF_KEY_DATE,"")


}






