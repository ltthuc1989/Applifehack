package com.ltthuc.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class RewardedAdsManager (private val context: Context) {

    init {
        loadRewardedAd()
    }

    private val TAG = "RewardedAdsManager"
    private var rewardedAd: RewardedAd? = null

    /** load Interstitial Ad */
    private fun loadRewardedAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            BuildConfig.ADS_REWARD,
            adRequest,
            object :  RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Ad Loaded")
                    rewardedAd = ad

                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.d(TAG, "Ad Failed to Load")
                    rewardedAd = null
                }
            }
        )
    }

    /** Show Ad if Available */
    fun showRewardedAdIfAvailable(activity: Activity, onCloseAd:() -> Unit = {}, onUserEarnedReward:(RewardItem) -> Unit = {}) {
        if (AdsSettings.disableAd) return
        if (AdsSettings.isRewardAdsShowing) {
            Log.d(TAG, "Ad Already Showing")
            return
        }

        if (rewardedAd == null) {
            Log.d(TAG, "Ad not Loaded")
            loadRewardedAd()
            return
        }

        //Show interstitial ad
        rewardedAd?.show(activity){
            onUserEarnedReward.invoke(it)
        }

        //Handle interstitial ad callback
        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "ad showed")
                AdsSettings.isRewardAdsShowing = true
            }

            override fun onAdDismissedFullScreenContent() {
                AdsSettings.isRewardAdsShowing = false
                onCloseAd.invoke()
                rewardedAd = null
                loadRewardedAd()
            }

            override fun onAdFailedToShowFullScreenContent(adLoadError: AdError) {
                Log.d(TAG, "FailedToShowFull: ${adLoadError.message}")
                onCloseAd.invoke()
            }
        }

    }

}
