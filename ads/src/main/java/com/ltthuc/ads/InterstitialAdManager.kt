package com.ltthuc.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.ltthuc.ads.AdsSettings.Companion.isInterstitialShowing
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.ltthuc.ads.AdsSettings.Companion.disableAd

class InterstitialAdManager(private val context: Context) {

    init {
        loadInterstitialAd()
    }

    private val TAG = "InterstitialAdManager"
    private var interstitialAd: InterstitialAd? = null

    /** load Interstitial Ad */
    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            BuildConfig.ADS_FULL_SCREEN_ID,
            adRequest,
            object : InterstitialAdLoadCallback(){
                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d(TAG, "Ad Loaded")
                    interstitialAd = ad

                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.d(TAG, "Ad Failed to Load")
                    interstitialAd = null
                }
            }
        )
    }

    /** Show Ad if Available */
    fun showInterstitialAdIfAvailable(activity: Activity, onCloseAd:() -> Unit = {}) {
        if (disableAd) return

        if (isInterstitialShowing) {
            Log.d(TAG, "Ad Already Showing")
            return
        }

        if (interstitialAd == null) {
            Log.d(TAG, "Ad not Loaded")
            loadInterstitialAd()
            return
        }

        //Show interstitial ad
        interstitialAd?.show(activity)

        //Handle interstitial ad callback
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "ad showed")
                isInterstitialShowing = true
            }

            override fun onAdDismissedFullScreenContent() {
                isInterstitialShowing = false
                interstitialAd = null
                onCloseAd.invoke()
                loadInterstitialAd()
            }

            override fun onAdFailedToShowFullScreenContent(adLoadError: AdError) {
                Log.d(TAG, "FailedToShowFull: ${adLoadError.message}")
            }
        }

    }

}
