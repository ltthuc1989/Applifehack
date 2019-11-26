package com.ltthuc.habit.ui.activity.webview

import com.ezyplanet.core.ui.base.MvvmNav


interface WebViewNav :MvvmNav{
    fun sendEmail(url:String?)
    fun openPdfFile(url:String?)
    fun showWebcontent(url:String?)
    fun onLoaded()

}