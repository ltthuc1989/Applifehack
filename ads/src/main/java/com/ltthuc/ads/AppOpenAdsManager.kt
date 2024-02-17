package com.ltthuc.ads

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.ltthuc.ads.AdsSettings.Companion.isInterstitialShowing
import com.ltthuc.ads.AdsSettings.Companion.isSplashScreen
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.ltthuc.ads.AdsSettings.Companion.isRewardAdsShowing
import java.util.Date

class AppOpenAdsManager(
    private val mBaseApp: Application
): Application.ActivityLifecycleCallbacks, LifecycleEventObserver {

    private val TAG = "AppOpenAdsManager"
    private var appOpenAd: AppOpenAd? = null
    private var appOpen: AppOpen? = null
    private var isAdLoading = false
    private var isAdShowing = false
    private var loadTime: Long = 0
    private var currentActivity: Activity? = null

    init {
        if (!AdsSettings.disableAd || AdsSettings.disableOpenAds) {
            mBaseApp.registerActivityLifecycleCallbacks(this)
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        }
    }


    /** Set AppOpen */
    fun setAppOpen(appOpen: AppOpen){
        if (AdsSettings.disableAd || AdsSettings.disableOpenAds) return
        this.appOpen = appOpen
    }

    /** Load AppOpenAd */
    private fun loadAd(){
        if (isAdLoading){
            Log.d(TAG, "Ad Already available or loading")
            return
        }
        isAdLoading = true
        val adRequest = AdRequest.Builder().build()
        AppOpenAd.load(
            mBaseApp.applicationContext,
            BuildConfig.ADS_APP_OPEN_ID,
            adRequest,
            appOpenLoadCallback
        )
    }

    private val appOpenLoadCallback = object : AppOpenAdLoadCallback(){
        override fun onAdLoaded(ad: AppOpenAd) {
            appOpenAd = ad
            loadTime = Date().time
            isAdLoading = false
            Log.d(TAG, "Ad Loaded")
        }

        override fun onAdFailedToLoad(error: LoadAdError) {
            isAdLoading = false
            appOpenAd = null
            Log.d(TAG, "Ad Failed to load: $error")
        }
    }

    /** Show AppOpenAd If Available */
    private fun showAdIfAvailable(activity: Activity){
        Log.d(TAG, "showAdIfAvailable Entry ${AdsSettings.disableAd}")
        if (AdsSettings.disableAd || AdsSettings.disableOpenAds) return
        Log.d(TAG, "showAdIfAvailable")
        if (isAdShowing) {
            Log.d(TAG, "Ad already Showing")
            return
        }

        if (!isAdAvailable()){
            Log.d(TAG, "Ad not available")
            loadAd()
            return
        }
        if (isSplashScreen) return

        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback(){

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad Showed")
                isAdShowing = true
                appOpen?.closeAds()
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad Dismissed")
                isAdShowing = false
                appOpenAd = null
                appOpen?.restoreAds()
                loadAd()
            }

            override fun onAdFailedToShowFullScreenContent(adLoadError: AdError) {
                Log.e(TAG, adLoadError.message)
                isAdShowing = false
                appOpenAd = null
                loadAd()
            }
        }

        Handler(activity.mainLooper).postDelayed({
            appOpenAd?.show(activity)
        }, 200)


    }

    private fun isAdAvailable(): Boolean {
        return appOpen != null && wasLoadTimeLessThanNHoursAgo(4)
    }

    private fun wasLoadTimeLessThanNHoursAgo(numHours: Int): Boolean {
        val dateDifference: Long = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    /** Lifecycle callbacks */
    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityStopped(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

        if (event == Lifecycle.Event.ON_RESUME){

            /** if you are using SplashActivity use "currentActivity == SplashActivity()".
             * if you are using SplashFragment use companion Object member "isSplashScreen: Boolean" */

            if (!isInterstitialShowing && !isRewardAdsShowing){
                showAdIfAvailable(currentActivity!!)
            }else{
                Log.d(TAG, "Cant Show Ad Right Now")
            }
        }

    }


}
