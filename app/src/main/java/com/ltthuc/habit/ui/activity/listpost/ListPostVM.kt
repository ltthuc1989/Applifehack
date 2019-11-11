package com.ltthuc.habit.ui.activity.listpost

import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.ltthuc.habit.data.AppDataManager
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.data.network.response.RssCatResp
import com.ltthuc.habit.ui.widget.TextObserVable
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class ListPostVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<ListPostNav, String>(schedulerProvider, connectionManager) {

    val results = NonNullLiveData<List<PostContent>>(emptyList())
    private val mData = ArrayList<PostContent>()
    private var currentPage = 1
   private var URL:String?=null
    val pageText = TextObserVable()
    var parser:Parser?=null
    private var isNext=true
   lateinit var rssCatResp: RssCatResp


    fun setModel(resp: RssCatResp){
        if(parser==null){
            parser = Parser()
        }
        pageText.text = currentPage.toString()
        rssCatResp = resp
         URL = resp.feed
         getFeed(currentPage,false)


    }

    fun onItemClicked(item: RssCatResp) {

    }

   private fun   getFeed(page:Int?=1,isChange:Boolean=true) {
        val temUrl=if(page==1) URL else "$URL&paged=$page"
        uiScope.launch{
            try {
                navigator?.showProgress()
                val articleList = parser?.getArticles("$temUrl")
                val posts = toPostList(articleList!!)
                mData.addAll(posts)

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
            getFeed(temp)
        }else{
            resetData()
            getFeed(page,false)
        }


    }
    fun prePage(){
        val page = pageText.text?.toInt()
        if(page==currentPage) {
            val temp= currentPage-1
            if(temp==0) return
            isNext = false
            resetData()
            getFeed(temp)
        }else{
            isNext = false
            resetData()
            getFeed(page,false)
        }

    }

    private fun resetData(){
        if(!mData.isEmpty()) {
            mData.clear()
            navigator?.resetState()
        }
    }

    fun postNow(item:PostContent){
        val post = item.toPost(rssCatResp)

        navigator?.showProgress()
        compositeDisposable.add(appDataManager.createPost(post).compose(schedulerProvider
            ?.ioToMainCompletableScheduler())
            .subscribe({


                navigator?.hideProgress()


            }, {
                navigator?.hideProgress()
                navigator?.showAlert(it.message)
            }))
    }


}