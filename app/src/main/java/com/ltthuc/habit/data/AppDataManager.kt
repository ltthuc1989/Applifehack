package com.ltthuc.habit.data

import android.content.Context
import android.telephony.TelephonyManager
import com.androidhuman.rxfirebase2.firestore.model.Value
import com.ezyplanet.thousandhands.shipper.data.preferences.AppPreferenceHelper
import com.google.firebase.firestore.QuerySnapshot

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


    override fun updateApiHeader(userId: Long?, accessToken: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setUserAsLoggedOut() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getApiHeader(): ApiHeader {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRssCat(): Single<Value<QuerySnapshot>> {
        return apiHelper.getRssCat()
    }
}