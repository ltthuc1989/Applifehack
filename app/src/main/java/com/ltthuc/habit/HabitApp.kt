package com.ltthuc.habit

import android.app.Activity
import android.app.Application
import android.app.Service
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.interceptors.HttpLoggingInterceptor
import com.crashlytics.android.Crashlytics
import com.facebook.FacebookSdk
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.firebase.FirebaseApp
import com.ltthuc.habit.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import io.fabric.sdk.android.Fabric
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HabitApp : Application(), HasActivityInjector, HasServiceInjector {
    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    // @Inject   lateinit var locationHelper: LocationHelper


    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(this)

        initDebugModeValues()
        initOneSignal()
        initCrashLytics()
        FirebaseApp.initializeApp(this)
        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)

        val okHttpClient = OkHttpClient().newBuilder()
                .addNetworkInterceptor(StethoInterceptor())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

        AndroidNetworking.initialize(this, okHttpClient)
        // disable Logging to upload photo
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY)
        }

    }

    private fun initDebugModeValues() {
//        Stetho.initializeWithDefaults(this)
    }

    /**
     * @return android dispatching injector for providing dependencies in android activities
     */
    override fun activityInjector(): AndroidInjector<Activity>? {
        return activityDispatchingAndroidInjector
    }

    override fun serviceInjector(): AndroidInjector<Service> {
        return dispatchingServiceInjector
    }

    internal fun initOneSignal() {
    }

    private fun initCrashLytics() {
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
        }
    }
}
