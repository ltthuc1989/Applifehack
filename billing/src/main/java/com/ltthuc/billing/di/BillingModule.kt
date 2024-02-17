package com.ltthuc.billing.di

import android.content.Context
import com.ltthuc.billing.BuildConfig
import com.ltthuc.billing.data.OrdersID
import com.ltthuc.billing.helper.BillingManager

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface BillingModule {

    companion object {

        @Singleton
        @Provides
        fun provideBilling(@ApplicationContext context: Context) = BillingManager(
            context = context,
            nonConsumableKeys = listOf(OrdersID.REMOVE_ADS),
            key = BuildConfig.LICENCE_KEY,
            enableLogging = true
        )

    }
}