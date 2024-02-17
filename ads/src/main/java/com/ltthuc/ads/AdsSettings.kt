package com.ltthuc.ads

import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

class AdsSettings {

    companion object{
        var isSplashScreen = true
        var isInterstitialShowing = false
        var isRewardAdsShowing = false
        var adPos: Int = 0
        var disableAd = false
        var disableOpenAds = false

        fun addDeviceTest() {
            if (BuildConfig.DEBUG) {
                val testDeviceIds: List<String> = mutableListOf(
                    "96787287227250A15AC1601146FA1593",
                    "CA359F0A8359CF87C7FE15B70F249878"
                )
                val configuration =
                    RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
                MobileAds.setRequestConfiguration(configuration)
            }
        }

        fun disableAd(disable: Boolean) {
            disableAd = disable
        }
    }

}