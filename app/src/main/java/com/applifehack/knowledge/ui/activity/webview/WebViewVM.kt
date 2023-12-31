package com.applifehack.knowledge.ui.activity.webview

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.MutableLiveData

import com.ezyplanet.core.ui.base.BaseViewModel

import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import javax.inject.Inject


open class WebViewVM @Inject constructor(connectionManager: BaseConnectionManager)
    : BaseViewModel<WebViewNav,String>(connectionManager) {
    val url = MutableLiveData<String>()
    val title = MutableLiveData<String>()
    val webViewChromeClient: WebChromeClient by lazy {
        ChromeClient()
    }

    val webViewClient: WebViewClient by lazy {
        if (url?.value?.contains("insurance-policy") == false) {

            WebViewClient()

        } else {

            Client()
        }

    }


    private inner class ChromeClient : WebChromeClient() {


        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if (newProgress <= 80)
                navigator?.showProgress()
            else if (newProgress > 80)
                navigator?.hideProgress()

        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)


        }
    }

    private inner class Client : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {

                if (!url.contains("pdf")) {
                    navigator?.showWebcontent(url)
                } else {
                    navigator?.openPdfFile(url)
                }
                return true
            } else {
                if (url?.contains("mail") == true) {
                    navigator?.sendEmail(url)

                }
                return true
            }
        }
    }





}