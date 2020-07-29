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
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.data.firebase.PayloadResult
import com.applifehack.knowledge.ui.activity.BaseBottomVM
import com.applifehack.knowledge.util.AppBundleKey
import com.applifehack.knowledge.util.RateUs
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

import javax.inject.Inject


class HomeVM @Inject constructor( appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseBottomVM<HomeNav, String>(appDataManager,schedulerProvider, connectionManager) {

    @Inject
    lateinit var fbAnalytics: FirebaseAnalyticsHelper
    init {
        homeClick()
    }

    fun getVersionCode(): Int? = appDataManager?.appPreferenceHelper?.versionUpdate

    fun showRateUse(activity: HomeActivity){


        if(appDataManager.appPreferenceHelper.appLaunchCount==4){
            RateUs.rate(activity,appDataManager.appPreferenceHelper)
        }
    }

    override fun handleIntent(intent: Intent?, isOnNewIntent: Boolean?) {
        if(isOnNewIntent!=true) subcribePush()

        val payloadResult = intent?.getParcelableExtra<PayloadResult>(AppBundleKey.KEY_NOTIFICATION)
        if(payloadResult!=null){
            (navigator as HomeNav).openDynamicLink(payloadResult.postId!!)
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
                    logEvent(postId,"openDynamicLink")
                    ( navigator as HomeNav).openDynamicLink(postId!!)
                }

            }
            .addOnFailureListener(context) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }
    }

    private fun logEvent(id: String?, action: String) {
        val event = "$${id}_$action"
        val str = "app_dynamic_link"
        fbAnalytics.logEvent(event, event, str)



    }

}