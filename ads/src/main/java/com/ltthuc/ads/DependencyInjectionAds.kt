package com.ltthuc.ads

import android.app.Application
import android.content.Context
import com.ltthuc.ads.BannerAd
import com.ltthuc.ads.GoogleMobileAdsConsentManager
import com.ltthuc.ads.InterstitialAdManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DependencyInjectionAds {

    @Singleton
    @Provides
    fun providesInterstitialAdsInstance(
        @ApplicationContext context: Context
    ): InterstitialAdManager {
        return InterstitialAdManager(context)
    }

    @Singleton
    @Provides
    fun providesAppOpenAdInstance(
        @ApplicationContext context: Context
    ): AppOpenAdsManager {
        return AppOpenAdsManager(context as Application)
    }

    @Singleton
    @Provides
    fun providesBannerAdInstance(
        @ApplicationContext context: Context
    ): BannerAd {
        return BannerAd(context)
    }
    
    @Singleton
    @Provides
    fun providesGoogleMobileAdsConsentManagerInstance(
        @ApplicationContext context: Context
    ): GoogleMobileAdsConsentManager {
        return GoogleMobileAdsConsentManager(context)
    }

    @Singleton
    @Provides
    fun providesRewardedAdInstance(
        @ApplicationContext context: Context
    ): RewardedAdsManager {
        return RewardedAdsManager(context)
    }


}
