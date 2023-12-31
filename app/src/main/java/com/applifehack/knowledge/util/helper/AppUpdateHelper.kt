package com.applifehack.knowledge.util.helper

import android.app.Activity
import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.applifehack.knowledge.BuildConfig
import com.ezyplanet.thousandhands.util.livedata.HotEventRx
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.InstallStatus


class AppUpdateHelper :LifecycleObserver,InstallStateUpdatedListener, MutableLiveData<HotEventRx<Int>>() {
    lateinit var  appUpdateManager :AppUpdateManager

    companion object {
        const val REQUEST_CODE_UPDATE = 1000
    }

    fun checkUpdate(context: Context) {


        appUpdateManager = AppUpdateManagerFactory.create(context)


        appUpdateManager.registerListener(this)

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if(appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)){
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    context as Activity,
                    // Include a request code to later monitor this update request.
                    REQUEST_CODE_UPDATE)
            } else if(appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED){
                postValue(HotEventRx(appUpdateInfo.installStatus()))
            }

        }
    }



    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        appUpdateManager?.unregisterListener(this)

    }


    override fun onStateUpdate(installState: InstallState?) {

        if (installState?.installStatus() == InstallStatus.DOWNLOADED) {
            postValue(HotEventRx(InstallStatus.DOWNLOADED))
        }
    }
    fun completeUpdate(){
        appUpdateManager.completeUpdate()
    }
}