package com.ltthuc.habit.util.helper


import android.app.Application
import android.content.Context
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.ezyplanet.core.util.rxlifecycle.RxLifecycle
import com.google.android.gms.ads.*
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAdListener



import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.ArrayList


import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

class AdsManager(private val application: Application, admobApp: String, private val debug: Boolean) :LifecycleObserver{
    protected var adViewList: MutableList<AdView> = ArrayList<AdView>()
    private var enableLogs = true
    private var adInvisibilityOnDebug = View.INVISIBLE

    init {
        MobileAds.initialize(application, admobApp)
    }

    constructor(application: Application, @StringRes admobApp: Int, debug: Boolean) : this(application, application.getString(admobApp), debug) {}

    fun showAdsOnDebug(showAdsOnDebug: Boolean): AdsManager {
        var showAdsOnDebug = showAdsOnDebug
        showAdsOnDebug = showAdsOnDebug
        return this
    }

    fun setEnableLogs(enableLogs: Boolean) {
        this.enableLogs = enableLogs
    }

    fun setAdInvisibilityOnDebug(adVisibilityOnDebug: Int) {
        this.adInvisibilityOnDebug = adVisibilityOnDebug
    }

    fun loadAndShowInterstitial(id: Int): Single<Boolean> {
        return if (!showAdsOnDebug && debug) {
            Single.just(true)
        } else {
            Single.create(SingleOnSubscribe<Boolean> { e ->
                val interstitialAd = InterstitialAd(application)
                val adRequestBuilder = AdRequest.Builder()
                adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                if (debug) {
                    adRequestBuilder.addTestDevice(DeviceIdFounder.getDeviceId(application))
                }
                interstitialAd.setAdUnitId(application.getString(id))

                interstitialAd.setAdListener(object : AdListener() {

                   override fun onAdLoaded() {
                        super.onAdLoaded()
                        interstitialAd.show()
                    }

                    override fun onAdFailedToLoad(i: Int) {
                        super.onAdFailedToLoad(i)
                        log("onAdFailedToLoad $i")
                        e.onError(AdError())
                    }

                   override fun onAdClosed() {
                        super.onAdClosed()
                        log("onAdClosed")
                        e.onSuccess(true)
                    }
                })
                interstitialAd.loadAd(adRequestBuilder.build())
            })
                    .subscribeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun log(text: String) {
        if (enableLogs) {
            Log.d(TAG, text)
        }
    }

    fun executeAdView(lifecycleOwner: LifecycleOwner, adView: AdView) {
        val compositeDisposable = CompositeDisposable()


        RxLifecycle.with(lifecycleOwner)
                .onResume()
                .doOnSubscribe(Consumer<Disposable> { d -> compositeDisposable.add(d) })
                .subscribe(Consumer<Any> { adView.resume() })

        RxLifecycle.with(lifecycleOwner)
                .onPause()
                .doOnSubscribe(Consumer<Disposable> { d -> compositeDisposable.add(d) })
                .subscribe(Consumer<Any> { adView.pause() })

        RxLifecycle.with(lifecycleOwner)
                .onDestroy()
                .doOnSubscribe(Consumer<Disposable> { d -> compositeDisposable.add(d) })
                .subscribe(Consumer<Any> {
                    adView.destroy()
                    compositeDisposable.clear()
                })

        if (debug && !showAdsOnDebug) {
            adView.setVisibility(adInvisibilityOnDebug)
        } else {
            val adRequestBuilder = AdRequest.Builder()
            if (debug) {
                adRequestBuilder.addTestDevice(DeviceIdFounder.getDeviceId(application))
            }
            adView.loadAd(adRequestBuilder.build())
            adViewList.add(adView)
        }
    }

    private fun loadAndshowRewardedVideo(id: String): Single<Boolean> {
        return Single.create { e ->
            val mAd = MobileAds.getRewardedVideoAdInstance(application)
            mAd.setRewardedVideoAdListener(object : RewardedVideoAdListener {

                internal var rewarded = false

               override fun onRewardedVideoAdLoaded() {
                    mAd.show()
                }

               override fun onRewardedVideoAdOpened() {

                }

               override fun onRewardedVideoStarted() {

                }

               override fun onRewardedVideoAdClosed() {
                    e.onSuccess(rewarded)
                }

              override  fun onRewarded(rewardItem: RewardItem) {
                    rewarded = true
                }

              override  fun onRewardedVideoAdLeftApplication() {

                }

              override  fun onRewardedVideoAdFailedToLoad(i: Int) {
                    e.onError(AdError())
                }

                override fun onRewardedVideoCompleted() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })

            val adRequestBuilder = AdRequest.Builder()
            adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            if (debug) {
                adRequestBuilder.addTestDevice(DeviceIdFounder.getDeviceId(application))
            }

            mAd.loadAd(id, adRequestBuilder.build())
        }
    }

    fun insertAdView(lifecycleOwner: LifecycleOwner, adContainer: ViewGroup, adUnitId: String, adSize: AdSize) {
        val adView = AdView(adContainer.context)
        adView.setAdSize(adSize)
        adView.setAdUnitId(adUnitId)
        adContainer.addView(adView)
        executeAdView(lifecycleOwner, adView)
    }

    fun insertAdView(lifecycleOwner: LifecycleOwner, adContainer: ViewGroup, @StringRes adUnitId: Int, adSize: AdSize) {
        insertAdView(lifecycleOwner, adContainer, application.getString(adUnitId), adSize)
    }

    object DeviceIdFounder {
        fun getDeviceId(context: Context): String {
            val android_id = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            return md5(android_id).toUpperCase()
        }

        fun md5(s: String): String {
            try {
                // Create MD5 Hash
                val digest = MessageDigest
                        .getInstance("MD5")
                digest.update(s.toByteArray())
                val messageDigest = digest.digest()

                // Create Hex String
                val hexString = StringBuffer()
                for (i in messageDigest.indices) {
                    var h = Integer.toHexString(0xFF and messageDigest[i].toInt())
                    while (h.length < 2)
                        h = "0$h"
                    hexString.append(h)
                }
                return hexString.toString()

            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }

            return ""
        }
    }

    private inner class AdError : Throwable()

    companion object {

        private val TAG = "AdsManager"
        private val showAdsOnDebug = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause(){


    }

}