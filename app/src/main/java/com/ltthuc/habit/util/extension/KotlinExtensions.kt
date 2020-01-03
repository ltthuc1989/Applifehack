package com.ltthuc.habit.util.extension

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.extension.gotoActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot

import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.ui.activity.webview.WebViewActivity
import com.ltthuc.habit.util.CustomTabHelper
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

import android.graphics.Bitmap
import androidx.core.content.ContextCompat.startActivity

import androidx.core.content.FileProvider
import java.io.File
import androidx.core.content.ContextCompat.startActivity
import com.google.common.io.Flushables.flush
import android.R.attr.bitmap
import android.R
import com.ltthuc.habit.BuildConfig
import java.io.FileOutputStream


fun MvvmActivity<*,*>.openLink(url: String?, customTabHelper: CustomTabHelper) {
    val builder = CustomTabsIntent.Builder()

    // modify toolbar color
    builder.setToolbarColor(ContextCompat.getColor(this, com.ltthuc.habit.R.color.colorPrimary))

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
        gotoActivity(WebViewActivity::class, mapOf(WebViewActivity.URL to url))
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
fun MvvmActivity<*,*>.shareImage(bitmap: Bitmap?=null){


    try {
    var CODE_FOR_RESULT = 981
        val file = File(this.externalCacheDir, "quote.png")
        val fOut = FileOutputStream(file)
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fOut)
        fOut.flush()
        fOut.close()
        file.setReadable(true, false)
        val intent = Intent(android.content.Intent.ACTION_SEND)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file)
        // intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file.getC));
        intent.putExtra(Intent.EXTRA_STREAM, photoURI)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/png"
        //startActivity(Intent.createChooser(intent, "Share image via"));
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


