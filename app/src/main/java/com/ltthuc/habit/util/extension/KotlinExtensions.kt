package com.ltthuc.habit.util.extension

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.extension.gotoActivity
import com.ltthuc.habit.R
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.ui.activity.webview.WebViewActivity
import com.ltthuc.habit.util.CustomTabHelper
import kotlin.reflect.KClass


fun MvvmActivity<*,*>.gotoPostDetail(post: Post,customTabHelper: CustomTabHelper) {
    val builder = CustomTabsIntent.Builder()

    // modify toolbar color
    builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))

    // add share button to overflow menu
    builder.addDefaultShareMenuItem()

    val anotherCustomTab = CustomTabsIntent.Builder().build()

    val requestCode = 100
    val intent = anotherCustomTab.intent
    intent.setData(Uri.parse(post.webLink))

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
    val packageName = customTabHelper.getPackageNameToUse(this, post?.url!!)

    if (packageName == null) {
        // if chrome not available open in web view
        gotoActivity(WebViewActivity::class, mapOf(WebViewActivity.URL to post?.url))
    } else {
        customTabsIntent.intent.setPackage(packageName)
        customTabsIntent.launchUrl(this, Uri.parse(post?.url))
    }
}