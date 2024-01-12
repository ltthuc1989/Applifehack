package com.applifehack.knowledge.ui.activity.webview

import android.util.Log
import android.webkit.JavascriptInterface
import com.applifehack.knowledge.data.network.response.RssCatResp

class WebViewJSInterface (val rssCatResp: RssCatResp){
    @JavascriptInterface
    fun processHTML(html: String?) {
        // process the html as needed by the app
        Log.d("WebViewJSInterface", "WebViewVM ${html}")

        rssCatResp.youtubeHtml = html!!
        rssCatResp?.event?.postValue(html)


    }
}