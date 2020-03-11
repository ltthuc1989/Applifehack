package com.ltthuc.habit.ui.activity.home

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.ltthuc.habit.R
import com.ltthuc.habit.data.AppDataManager
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.data.entity.PostType
import com.ltthuc.habit.data.firebase.PayloadResult
import com.ltthuc.habit.util.AppBundleKey
import com.ltthuc.habit.util.RateUs

import javax.inject.Inject


class HomeVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<HomeNav, String>(schedulerProvider, connectionManager) {


    fun getVersionCode(): Int? = appDataManager?.appPreferenceHelper?.versionUpdate

    fun showRateUse(activity: HomeActivity){


        if(appDataManager.appPreferenceHelper.appLaunchCount==4){
            RateUs.rate(activity,appDataManager.appPreferenceHelper)
        }
    }

    fun handleIntent(intend:Intent){
        subcribePush()
        val payloadResult = intend?.getParcelableExtra<PayloadResult>(AppBundleKey.KEY_NOTIFICATION)
        if(payloadResult!=null){
            when(payloadResult.getPostType()){
                PostType.ARTICLE->{
                  navigator?.openArtilce(payloadResult?.link)
                }
                PostType.VIDEO->{
                 navigator?.openVideo(payloadResult?.link)
                }
                PostType.QUOTE->{
                navigator?.openQuote()
                }
            }
        }
    }

    fun subcribePush() {
        if (appDataManager.appPreferenceHelper.enableNotification == true) {
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { it1 ->

                FirebaseMessaging.getInstance().subscribeToTopic("all").addOnSuccessListener {

                }
            }
        }
    }

}