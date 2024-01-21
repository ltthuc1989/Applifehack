package com.applifehack.knowledge.ui.activity

import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.network.response.RssCatResp
import com.applifehack.knowledge.ui.activity.webview.WebViewJavaScriptLoad
import com.applifehack.knowledge.ui.admin.rsspost.URLWraper
import com.applifehack.knowledge.ui.admin.rssposts.RssListModel
import com.ezyplanet.supercab.data.local.db.DbHelper
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import java.util.ArrayList
import javax.inject.Inject

class RSSVM @Inject constructor(val appDataManager: AppDataManager, val dbHelper: DbHelper,schedulerProvider: SchedulerProvider,connectionManager: BaseConnectionManager
) : BaseViewModel<RSSNav, String>(schedulerProvider, connectionManager) {

    val results = NonNullLiveData<List<RssCatResp>>(emptyList())
    private val mData = ArrayList<RssCatResp>()
    private val urlWrapers = ArrayList<URLWraper>()
    private var currentPage = 1
    private var isExport = false



    fun getWebClient():ChromeClient{
        return ChromeClient()
    }

    fun getClient(): WebViewClient {
        return Client()
    }

    fun getRssCat() {
        navigator?.showProgress()
        compositeDisposable.add(appDataManager.getRssCat().compose(schedulerProvider?.ioToMainSingleScheduler())
                .map {
                    it.value().toObjects(RssCatResp::class.java)
                }.subscribe({

                    mData.addAll(it)
                    results.value = mData
                    setUrlWrapers(it)
                    navigator?.hideProgress()


                }, {
                    navigator?.hideProgress()
                    navigator?.showAlert(it.message)
                }))

    }


    fun onItemClicked(item:RssCatResp){
       // scrapData(item)
        val rssModel = RssListModel(feedUrl = item.feed, feedPageUrl = item.feedPageUrl, type = item.type)
       navigator?.gotoListPostScreen(rssModel)

    }

    private fun setUrlWrapers(data:List<RssCatResp>){
        data.forEach {
            if(it.author_name?.contains("howstuffworks") == true) {
                it.feed = "https://science.howstuffworks.com/"
                urlWrapers.add(URLWraper(it,1))
                it.feed = "https://animals.howstuffworks.com/"
                urlWrapers.add(URLWraper(it,1))
                it.feed = "https://people.howstuffworks.com/culture"
                urlWrapers.add(URLWraper(it,1))
                it.feed = "https://electronics.howstuffworks.com/tech"
                urlWrapers.add(URLWraper(it,1))
            } else {
                urlWrapers.add(URLWraper(it,1))
            }

        }


    }



   fun fetchAndSaveDatabase(){
       navigator?.showProgress()
       var sumPost = 0
       uiScope.launch {
           var asyns = mutableListOf<Deferred<List<Post>>>()
           urlWrapers.forEach {
               val data = async(Dispatchers.IO) {
                   Log.d("updateDatabase","async")
//                   var doc = Jsoup.connect(it.pageUrl()).get()
//                   it.cssQuery(doc)
                   val rssCatResp = it.rssCatResp
                   if(!it.rssCatResp.json) {
                       val doc = if (rssCatResp.type != "video")
                           Jsoup.connect(it.pageUrl()).header("Content-Type","application/x-www-form-urlencoded").timeout(10*6000).get()
                       else Jsoup.parse(rssCatResp.youtubeHtml)
                       rssCatResp.cSSQuery(doc)
                   }else{
                       rssCatResp.cSSQuery(null)
                   }



               }
               asyns.add(data)

           }
           withContext(Dispatchers.IO) {
               Log.d("updateDatabase", "updating")
               isExport = true
               var list = ArrayList<Post>()
               asyns.forEach {
                   val temp = it.await()
                      try {
                          Log.d("updateDatabase", ":${temp[0].author} -${temp.size}")
                      }catch (ex:Exception){
                         ex.printStackTrace()

                      }
                //  list.plus(temp)
                   sumPost+=temp.size
                   dbHelper.insertPost(temp)
                 //  Log.d("updateDatabase", "ListSize:${temp.size}")

               }


           }
           navigator?.hideProgress()
           Log.d("updateDatabase", "updated:$sumPost")


       }

   }




     class Client() : WebViewClient() {


        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            // view?.loadUrl(WebViewJavaScriptLoad().loadHtml)
        }

         override fun shouldOverrideUrlLoading(
             view: WebView?,
             request: WebResourceRequest?
         ): Boolean {
             return false
         }
    }




    inner class ChromeClient() : WebChromeClient() {


        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if (newProgress <= 80)
                // navigator?.showProgress()
            else if (newProgress > 80) {
                // navigator?.hideProgress()
                Log.d("RSSVM", "onProgressChanged: ${newProgress}")
                view?.loadUrl(WebViewJavaScriptLoad().loadHtml)
            }

        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)


        }


    }


}

