package com.ezyplanet.thousandhands.util.device

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log


import com.ezyplanet.thousandhands.shipper.data.preferences.AppPreferenceHelper
import com.ltthuc.habit.R

import javax.inject.Inject

class DeviceUtilImpl @Inject constructor(
        private val context: Context, private val appPreferencesHelper: AppPreferenceHelper
) : BaseDeviceUtil {

    override fun getAndroidVersion(): String {

        val release = Build.VERSION.RELEASE
        val sdkVersion = Build.VERSION.SDK_INT
        return "Android SDK: $sdkVersion ($release)"


    }

    override fun getUniqueId(): String {
//        if (appPreferencesHelper.uniqueId.isEmpty())
//            appPreferencesHelper.uniqueId = UUID.randomUUID().toString()
//
//        return appPreferencesHelper.uniqueId
        return ""
    }


    override fun getAppVersion(): String ?{
        try {
//            if(BuildConfig.BUILD_TYPE!="production"){
//                return context.getString(R.string.app_version)+" "+context?.getPackageManager()?.getPackageInfo(context?.getPackageName(), 0)?.versionName+"("+BuildConfig.BUILD_TYPE+")"
//
//            }else{
//                return context.getString(R.string.app_version)+" "+context?.getPackageManager()?.getPackageInfo(context?.getPackageName(), 0)?.versionName
//
//            }
            return context.getString(R.string.app_version)+" "+context?.getPackageManager()?.getPackageInfo(context?.getPackageName(), 0)?.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("About", e.message)
        }
       return ""
    }


}