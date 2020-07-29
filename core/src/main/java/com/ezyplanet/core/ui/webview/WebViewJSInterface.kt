package com.ezyplanet.core.ui.webview

import android.util.Log
import android.webkit.JavascriptInterface

class WebViewJSInterface (){
    @JavascriptInterface
    fun processHTML(html: String?) {
        // process the html as needed by the app
        Log.d("abcd",html)


    }
}