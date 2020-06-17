package com.applifehack.knowledge.ui.activity.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.entity.ArticleType
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.firebase.PayloadResult
import com.applifehack.knowledge.ui.activity.BaseBottomVM
import com.applifehack.knowledge.util.AppBundleKey
import com.applifehack.knowledge.util.RateUs
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

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
            favoriteSeleted.value = false
        }
    }
    fun catClick(){
        if(catSelected.value!=true) {
            catSelected.value = true
            homeSelected.value = false
            settingSelected.value = false
            favoriteSeleted.value = false
        }

    }

    fun favoriteClick(){
        if(favoriteSeleted.value!=true){
            favoriteSeleted.value = true
            homeSelected.value = false
            settingSelected.value = false
            catSelected.value = false
        }
    }

    fun settingClick(){

//        settingSelected.value = true
//        homeSelected.value =false
//        catSelected.value = false
    }

    fun getDynamicLink(intent: Intent,context: Activity){
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(context) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)

                if (pendingDynamicLinkData != null) {

                    val postId=pendingDynamicLinkData.link?.getQueryParameter("postId")
                    ( navigator as HomeNav).openDynamicLink(postId!!)
                }

            }
            .addOnFailureListener(context) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }
    }

}