package com.ltthuc.ads

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError


class BannerAd(private val context: Context) {

    private val adView = AdView(context).apply {
        adUnitId = BuildConfig.ADS_BANNER_ID
    }
    private var init = false

    @SuppressLint("MissingPermission")
    fun loadAndShowBannerAd(isAnchored: Boolean, bannerAdView: FrameLayout) {
        if (AdsSettings.disableAd) return
        if (bannerAdView.childCount != 0) return
        if (!init) {
            adView.adListener = object : AdListener() {
                override fun onAdClosed() {
                    super.onAdClosed()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    bannerAdView.visibility = View.GONE
                }

                override fun onAdOpened() {
                    super.onAdOpened()
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                }
            }

            if (isAnchored) {
                adView.setAdSize(adSize(bannerAdView))
            } else {
                adView.setAdSize(AdSize.BANNER)
            }
            init = true
        }


        if (adView.parent != null) {
            (adView.parent as ViewGroup).removeView(adView)
        } else {
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        }
        bannerAdView.addView(adView)
    }

    private fun adSize(bannerAdView: FrameLayout): AdSize {
        return if (SDK_INT >= Build.VERSION_CODES.R) {
            getSizeAboveAndroidApi30(bannerAdView)
        } else {
            getSizeBelowAndroidApi30(bannerAdView)
        }
    }

    @Suppress("DEPRECATION")
    private fun getSizeBelowAndroidApi30(bannerAdView: FrameLayout): AdSize {

        val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)


        var adWidthPixels = bannerAdView.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }

        val density = outMetrics.density
        val adWidth = (adWidthPixels / density).toInt()

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun getSizeAboveAndroidApi30(bannerAdView: FrameLayout): AdSize {
        val windowMetrics = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).currentWindowMetrics
        val bounds = windowMetrics.bounds

        var adWidthPixels = bannerAdView.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = bounds.width().toFloat()
        }
        val density = context.resources.displayMetrics.density
        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
    }

}
