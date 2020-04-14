package com.applifehack.knowledge

import android.app.Activity
import android.app.Service
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.interceptors.HttpLoggingInterceptor
import com.applifehack.knowledge.di.component.DaggerAppComponent
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.fabric.sdk.android.Fabric
import okhttp3.OkHttpClient
import org.sourcei.kowts.utils.functions.F
import org.sourcei.kowts.utils.reusables.Gradients
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class KnowledgeApp : MultiDexApplication(), HasActivityInjector {
    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    // @Inject   lateinit var locationHelper: LocationHelper
    var isBackGround = false


    override fun onCreate() {
        super.onCreate()
       // FacebookSdk.sdkInitialize(this)
        MultiDex.install(this)
        FirebaseApp.initializeApp(this)
        FirebaseApp.getInstance()

        initOneSignal()
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
        Gradients = F.readGradients(this)

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


    internal fun initOneSignal() {
    }

    private fun initCrashLytics() {
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
        }
    }


}
