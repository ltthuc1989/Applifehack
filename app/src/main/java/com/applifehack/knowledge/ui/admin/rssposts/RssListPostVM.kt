package com.applifehack.knowledge.ui.admin.rssposts

import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.MutableLiveData
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.network.response.RssCatResp
import com.applifehack.knowledge.ui.activity.webview.WebViewJavaScriptLoad
import com.applifehack.knowledge.ui.widget.TextObserVable
import com.applifehack.knowledge.util.MeasureTime
import com.applifehack.knowledge.util.extension.await
import com.ezyplanet.supercab.data.local.db.DbHelper
import com.prof18.rssparser.model.RssItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.ArrayList
import javax.inject.Inject

class RssListPostVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider,val dbHelper: DbHelper, connectionManager: BaseConnectionManager
) : BaseViewModel<RssListPostNav, String>(schedulerProvider, connectionManager) {

    val results = NonNullLiveData<List<Post>>(emptyList())
    private val mData = ArrayList<Post>()
    private var currentPage = 1
    private var URL:String?=null
    private var pageUrl:String?=null
    val pageText = TextObserVable()
    private var isNext=true
    lateinit var rssCatResp: RssCatResp
    val isVideo = MutableLiveData<Boolean>()
    private var isFirstLoad = false
    var sendPush = false
    var pushStyle = false



    fun setModel(resp: RssListModel){

        pageText.text = currentPage.toString()
        rssCatResp = RssCatResp().apply {
            feed = resp.feedUrl
            feedPageUrl = resp.feedPageUrl
            type = resp.type!!
            event = MutableLiveData<String>()
        }
        isVideo.value = rssCatResp.type =="video"
        URL = resp.feedUrl
        pageUrl = resp.feedPageUrl

//        getFeed(currentPage,false)



    }

    fun onItemClicked(item: RssCatResp) {

    }


   fun   getFeed(page:Int?=1,isChange:Boolean=true) {
        val temUrl=if(page==1) URL else "$pageUrl$page"
        uiScope.launch{
            try {
                val measureTime = MeasureTime()
                measureTime.start()
                navigator?.showProgress()
                //val articleList = parser?.getArticles("$temUrl")
               // val posts = toPostList(articleList!!)
              val post=  withContext(Dispatchers.IO){
                    if(rssCatResp.json==false) {
                        val doc = if (rssCatResp.type != "video") Jsoup.connect(temUrl).get()
                        else {

                            Jsoup.parse(rssCatResp.youtubeHtml)
                        }
                        rssCatResp.cSSQuery(doc)
                    }else{
                        rssCatResp.cSSQuery(null)
                    }
                }


                mData.addAll(async {filter(post)}.await())

                results.value = mData

                navigator?.hideProgress()
                isVideo.value = false
                if(isChange) {

                    if(isNext) {
                        currentPage += 1
                    }else{
                        currentPage-=1
                    }
                    isNext = true
                    pageText.text = "$currentPage"
                }
                measureTime.end()
            } catch (e: Exception) {
                e.printStackTrace()
                navigator?.hideProgress()
                navigator?.showAlert(e.message)

            }
        }
    }

    private suspend fun toPostList(data: List<RssItem>) = withContext(Dispatchers.Default) {
        val result = mutableListOf<PostContent>()
        data.forEach {
            result.add(PostContent(it))

        }
        result
    }

    fun nextPage(){
        if(rssCatResp.type=="video"&& isVideo.value ==false){
            navigator?.showAlert("meessage","show web")
            return
        }
        val page = pageText.text?.toInt()
        if(page==currentPage) {
            val temp = currentPage + 1
            resetData()
            getYoutubeDoc(temp)
           //getFeed(temp)
        }else{
            resetData()
            getYoutubeDoc(page)
            //getFeed(page,false)
        }


    }
    fun prePage(){
        if(rssCatResp.type=="video"&& isVideo.value ==false){
            navigator?.showAlert("meessage","show web")
            return
        }
        val page = pageText.text?.toInt()
        if(page==currentPage) {
            val temp= currentPage-1
            if(temp==0) return
            isNext = false
            resetData()
            getYoutubeDoc(temp)
           // getFeed(temp)
        }else{
            isNext = false
            resetData()
            getYoutubeDoc(page)
            //getFeed(page,false)
        }

    }


    private fun resetData(){
        if(!mData.isEmpty()) {
            mData.clear()
            navigator?.resetState()
        }
    }

    fun postNow(item: Post){


        uiScope?.launch {
            navigator?.showProgress()
            appDataManager.createPost(item.id, item.apply {
                if (sendPush) {
                    send_push = 1
                    push_style = if (pushStyle) 1 else 0
                }
            }).addOnFailureListener {
                navigator?.hideProgress()
                navigator?.showAlert(it.message)

            }.addOnSuccessListener {
                uiScope.launch {


                    withContext(Dispatchers.IO) {
                        dbHelper.insertPost(arrayListOf(item))
                    }
                    mData.remove(item)
                    bindToUI()
                    navigator?.hideProgress()
                }

            }



        }

    }





    private fun getYoutubeDoc(page: Int?,isChange: Boolean=true){
        if(rssCatResp.type=="video") {
            val temUrl=if(page==1) URL else {
                "$pageUrl$page"

            }
            navigator?.loadYoutubeUrl(temUrl)
           // getFeed(page, false)
        }else{
            getFeed(page,isChange)
        }


    }

   private suspend fun filter(posts:List<Post>) = withContext(Dispatchers.IO){
        val result = mutableListOf<Post>()
        posts.forEach {
            val temp = dbHelper.getPostById(it.id)
            if (temp == null) {
                result.add(it)
            }
        }
       result

    }
    fun skip(item: Post){
        navigator?.showProgress()
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dbHelper.insertPost(arrayListOf(item))
            }
            mData.remove(item)
            bindToUI()
            navigator?.hideProgress()
        }
    }

    private fun bindToUI(){
        results.value = mData
    }

    fun getWebClient():ChromeClient {
        return ChromeClient()
    }

    fun getClient(): WebViewClient {
        return Client()
    }
    class Client() : WebViewClient() {


        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return false
        }
    }
    fun showWeb(){

        if(rssCatResp.type=="video") {
            isVideo.value = isVideo.value != true
        }
    }

    fun checkPush(isChecked: Boolean) {
        sendPush = isChecked


    }

    fun checkPushStyle(isChecked: Boolean) {
        pushStyle = isChecked

    }

    fun openPostDetail(data: Post, shareView: View) {
//        uiScope?.launch {
//            try {
//
//                if(data?.getPostType()== PostType.VIDEO){
//                    navigator?.gotoYoutubeDetail(data,shareView)
//
//                }else{
//                    navigator?.gotoFeedDetail(data)
//                }
//
//
//                val resul = appDataManager.updateViewCount(data.id)
//                resul.await()
//
//            } catch (ex: Exception) {
//
//            }
//
//
//        }

    }


    inner class ChromeClient() : WebChromeClient() {


        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if (newProgress <= 80)
            // navigator?.showProgress()
            else if (newProgress > 80) {
                // navigator?.hideProgress()
                Log.d("RssListPostVM", "onProgressChanged: ${newProgress}")
                view?.loadUrl(WebViewJavaScriptLoad().loadHtml)
            }

        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)


        }



    }

}