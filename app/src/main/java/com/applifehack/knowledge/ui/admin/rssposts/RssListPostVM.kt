package com.applifehack.knowledge.ui.admin.rssposts

import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.network.response.RssCatResp
import com.applifehack.knowledge.ui.widget.TextObserVable
import com.prof.rssparser.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.util.ArrayList
import javax.inject.Inject

class RssListPostVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<RssListPostNav, String>(schedulerProvider, connectionManager) {

    val results = NonNullLiveData<List<Post>>(emptyList())
    private val mData = ArrayList<Post>()
    private var currentPage = 1
    private var URL:String?=null
    private var pageUrl:String?=null
    val pageText = TextObserVable()
    private var isNext=true
    lateinit var rssCatResp: RssCatResp


    fun setModel(resp: RssCatResp){

        pageText.text = currentPage.toString()
        rssCatResp = resp
        URL = resp.feed
        pageUrl = resp.feedPageUrl

       // getFeed(currentPage, false)
        getYoutubeDoc(currentPage,false)



    }

    fun onItemClicked(item: RssCatResp) {

    }


    private fun   getFeed(page:Int?=1,isChange:Boolean=true) {
        val temUrl=if(page==1) URL else "$pageUrl$page"
        uiScope.launch{
            try {
                navigator?.showProgress()
                //val articleList = parser?.getArticles("$temUrl")
               // val posts = toPostList(articleList!!)
              val post=  async(Dispatchers.IO) {

                    val doc = if(rssCatResp.type!="video")
                        Jsoup.connect(temUrl).get()
                    else Jsoup.parse(rssCatResp.youtubeHtml)
                   rssCatResp.cSSQuery(doc)
                }
                mData.addAll(post.await())

                results.value = mData

                navigator?.hideProgress()
                if(isChange) {

                    if(isNext) {
                        currentPage += 1
                    }else{
                        currentPage-=1
                    }
                    isNext = true
                    pageText.text = "$currentPage"
                }
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

    private suspend fun toPostList(data: List<Article>) = withContext(Dispatchers.Default) {
        val result = mutableListOf<PostContent>()
        data.forEach {
            result.add(PostContent(it))

        }
        result
    }

    fun nextPage(){
        val page = pageText.text?.toInt()
        if(page==currentPage) {
            val temp = currentPage + 1
            resetData()
            getYoutubeDoc(temp)
           // getFeed(temp)
        }else{
            resetData()
            getYoutubeDoc(page)
           // getFeed(page,false)
        }


    }
    fun prePage(){
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

//    fun postNow(item:Post){
//
//        uiScope?.launch {
//            val generateId = async(Dispatchers.IO) {
//                appDataManager.getGeneratePostId()
//            }
//            generateId?.await()?.let {
//
//                navigator?.showProgress()
//                appDataManager.createPost(it, item)
//                navigator?.hideProgress()
//
//            }
//        }
//
//    }
    fun postNow(item: Post){

        uiScope?.launch {
                navigator?.showProgress()
                appDataManager.createPost(item.id, item).addOnFailureListener {
                    navigator?.showAlert(it.message)
                }
                navigator?.hideProgress()


        }

    }

    fun updateToDB(){

    }


    private fun getYoutubeDoc(page: Int?,isChange: Boolean=true){
        if(rssCatResp.type=="video") {
            val temUrl=if(page==1) URL else "$pageUrl$page"
            apiSingle(appDataManager.getHtmlDoc(temUrl!!), {
                rssCatResp.youtubeHtml = it
                if (isChange) getFeed(page)
                else getFeed(page, false)
            },true)
        }else{
            getFeed(page,isChange)
        }


    }


}