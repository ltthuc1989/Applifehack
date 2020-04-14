package com.applifehack.knowledge.ui.activity.home

import android.content.Intent
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.firebase.PayloadResult
import com.applifehack.knowledge.ui.activity.BaseBottomVM
import com.applifehack.knowledge.util.AppBundleKey
import com.applifehack.knowledge.util.RateUs

import javax.inject.Inject


class HomeVM @Inject constructor( appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseBottomVM<HomeNav, String>(appDataManager,schedulerProvider, connectionManager) {


    init {
        homeClick()
    }

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

                    (navigator as HomeNav).openArtilce(payloadResult?.link)
                }
                PostType.VIDEO->{
                    (navigator as HomeNav).openVideo(payloadResult?.link)
                }
                PostType.QUOTE->{
                    (navigator as HomeNav).openQuote()
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

    fun homeClick(){
        if(homeSelected.value!=true) {
            homeSelected.value = true
            settingSelected.value = false
            catSelected.value = false
        }
    }
    fun catClick(){
        if(catSelected.value!=true) {
            catSelected.value = true
            homeSelected.value = false
            settingSelected.value = false
        }

    }

    fun settingClick(){

//        settingSelected.value = true
//        homeSelected.value =false
//        catSelected.value = false
    }

}