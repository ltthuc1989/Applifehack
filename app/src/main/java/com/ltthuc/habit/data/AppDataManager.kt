package com.ltthuc.habit.data

import android.content.Context
import android.telephony.TelephonyManager
import com.ezyplanet.thousandhands.shipper.data.preferences.AppPreferenceHelper

import com.google.gson.Gson
import com.irmansyah.catalogmoviekotlin.data.DataManager
import com.ltthuc.habit.data.network.ApiHeader
import com.ltthuc.habit.data.network.ApiHelper
import io.reactivex.Flowable
import io.reactivex.Single
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppDataManager @Inject constructor(val context: Context, val appPreferenceHelper: AppPreferenceHelper, private val apiHelper: ApiHelper) : DataManager {

    private var _deviceToken: String? = null
    var deviceToken: String?
        get() = _deviceToken
        set(value) {
            _deviceToken = value
        }



    private var _tripId: String? = null
    var tripId: String?
        get() = _tripId
        set(value) {
            _tripId = value
        }

    override fun updateApiHeader(userId: Long?, accessToken: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setUserAsLoggedOut() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getApiHeader(): ApiHeader {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}