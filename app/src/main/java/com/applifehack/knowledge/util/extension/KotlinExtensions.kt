package com.applifehack.knowledge.util.extension

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.applifehack.knowledge.BuildConfig
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.util.CustomTabHelper
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.ui.base.MvvmActivity
import com.google.android.gms.tasks.Task
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


fun MvvmActivity<*,*>.openLink(url: String?, customTabHelper: CustomTabHelper) {
    if(url ==null) return
    val builder = CustomTabsIntent.Builder()

    // modify toolbar color
    builder.setToolbarColor(ContextCompat.getColor(this, com.applifehack.knowledge.R.color.colorPrimary))

    // add share button to overflow menu
    builder.addDefaultShareMenuItem()

    val anotherCustomTab = CustomTabsIntent.Builder().build()

    val requestCode = 100
    val intent = anotherCustomTab.intent
    intent.setData(Uri.parse(url))

    val pendingIntent = PendingIntent.getActivity(this,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT)

    // add menu item to oveflow
    builder.addMenuItem("Sample item", pendingIntent)

    // menu item icon
    // val bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)
    // builder.setActionButton(bitmap, "Android", pendingIntent, true)

    // modify back button icon
    // builder.setCloseButtonIcon(bitmap)

    // show website title
    builder.setShowTitle(true)

    // animation for enter and exit of tab
    builder.setStartAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
    builder.setExitAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)

    val customTabsIntent = builder.build()

    // check is chrom available
    val packageName = customTabHelper.getPackageNameToUse(this, url!!)

    if (packageName == null) {
        // if chrome not available open in web view
        var browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    } else {
        customTabsIntent.intent.setPackage(packageName)
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }



}
fun MvvmActivity<*,*>.shareMessage(message:String){

    val intent2 = Intent()

    intent2.action=Intent.ACTION_SEND
    intent2.type="text/plain"
    intent2.putExtra(Intent.EXTRA_TEXT, message)
    startActivity(Intent.createChooser(intent2, "Share via"))
}
fun MvvmActivity<*,*>.shareImage(shortLink:String?){


    try {
        var CODE_FOR_RESULT = 981
        val shareTitle = String.format(getString(R.string.share_info_message,shortLink))
        val  file = File(File(cacheDir, "images"), "image.png")
        val intent = Intent(android.content.Intent.ACTION_SEND)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file)
        intent.setDataAndType(photoURI,contentResolver.getType(photoURI))
        intent.putExtra(Intent.EXTRA_STREAM, photoURI)
        intent.putExtra(Intent.EXTRA_TEXT,shareTitle)



        startActivityForResult(Intent.createChooser(intent, "Share image via"), CODE_FOR_RESULT)
    } catch (e: Exception) {
        e.printStackTrace()
    }


}
suspend fun <T> Task<T>.await(): T = suspendCoroutine { continuation ->
    addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation?.resume(task.result as T)
        } else {
            continuation.resumeWithException(task.exception ?: RuntimeException("Unknown task exception"))
        }
    }
}

fun BaseViewModel<*,*>.logEvent(id:String,fbAnalyticsHelper: FirebaseAnalyticsHelper){
    val param = "${id}_tap"
    fbAnalyticsHelper.logEvent(param,param,"knowledge_attribute")

}

inline fun <reified T> toArray(list: List<*>): Array<T> {
    return (list as List<T>).toTypedArray()
}


