package com.applifehack.knowledge.util

import android.os.Build
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import androidx.databinding.BindingAdapter
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.network.response.RssCatResp
import com.applifehack.knowledge.ui.activity.webview.WebViewJSInterface
import org.sourcei.kowts.utils.functions.F
import org.sourcei.kowts.utils.reusables.Angles


class BindingAdapters{
    companion object {
        @JvmStatic
        @BindingAdapter("viewSelected")
        fun setViewSelected(view: View, selected:Boolean) {
            view.isSelected = selected

        }

        @JvmStatic
        @BindingAdapter("gradient")
        fun setGradient(view: View, selected:Boolean) {
            val colors = F.randomGradient().toIntArray()
            val angle = Angles.random().toFloat()
            view.setGradient(colors, 0, angle)
        }
        @JvmStatic
        @BindingAdapter(value =["webClient","rssCat"])
        fun setWebClient(view: WebView, client: WebViewClient,rssCatResp: RssCatResp) {

            val settings =view.settings
            settings.loadWithOverviewMode = true
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.databaseEnabled = true
            settings.useWideViewPort = true
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            view.clearHistory()
            view.clearFormData()
            view.clearCache(true)
            settings.cacheMode = WebSettings.LOAD_NO_CACHE


                if (Build.VERSION.SDK_INT >= 21) {
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                settings.databasePath = "/data/data/" + view.context.packageName + "/databases/"
            }
            view.webViewClient = client
            view.addJavascriptInterface(WebViewJSInterface(rssCatResp),"$$")


        }
        @JvmStatic
        @BindingAdapter(value = ["ChromeClient", "rssCat"])
        fun setChromeClient(view: WebView, client: WebChromeClient, rssCatResp: RssCatResp) {

            val settings =view.settings
            settings.loadWithOverviewMode = true
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.databaseEnabled = true
            settings.useWideViewPort = true
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            settings.loadWithOverviewMode = true



            settings.cacheMode = WebSettings.LOAD_NO_CACHE



            if (Build.VERSION.SDK_INT >= 21) {
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                settings.databasePath = "/data/data/" + view.context.packageName + "/databases/"
            }



            view.webChromeClient = client
            view.addJavascriptInterface(WebViewJSInterface(rssCatResp),"$$")

        }
        @JvmStatic
        @BindingAdapter("loadUrl")
        fun loadUrl(view: WebView, url: String?) {
            view.loadUrl(url!!)
        }
    }


}