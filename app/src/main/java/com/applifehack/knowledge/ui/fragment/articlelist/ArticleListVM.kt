package com.applifehack.knowledge.ui.fragment.articlelist

import android.content.Context
import android.util.Log
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.util.SortBy
import com.applifehack.knowledge.util.extension.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

class ArticleListVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<ArticleListNav, String>(schedulerProvider, connectionManager) {

    val results = NonNullLiveData<List<Post>>(emptyList())
    private val mData = ArrayList<Post>()
    private var lastItem: DocumentSnapshot?=null
    private var currentPage=0
    lateinit var catId:String
    @Inject lateinit var fbAnalytics:FirebaseAnalyticsHelper
    override fun updateModel(data: String?) {
        catId = data!!
        getPost(catId)

    }


    fun getPost(catId:String?,nextPage: Boolean? = false) {

        if (nextPage == false) navigator?.showProgress()
        resetLoadingState = true
        uiScope?.launch {
            try {
                val data = appDataManager.getPostByCat(catId, SortBy.NEWEST, nextPage,lastItem)

                data?.await().let {
                    if(!it.isEmpty) {
                        val snapshot = async(Dispatchers.Default) {
                            it.toObjects(Post::class.java)
                        }


                        lastItem = it.documents[it.size() - 1]
                        val rs = snapshot.await()
                        mData.addAll(rs)
                        rs.forEach {
                            Log.d("PostTitle",it.title)
                        }


                        currentPage += 1
                    }
                    results.value = mData
                    resetLoadingState = false
                    navigator?.hideProgress()

                    Log.d("currentPage","$currentPage")
                    Log.d("Post size:","${mData.size}")
                }

            } catch (ex: Exception) {
                navigator?.hideProgress()
                resetLoadingState = false
            }

        }


    }

    fun onItemClicked(item: Post){
        navigator?.gotoPostDetail(item)
        tapEvent(item?.id)
    }

    fun onLoadMore(page: Int) {
        if(page==1) return
        getPost(catId,true)
    }

    fun shareClick(context:Context, data:Post){

            val download = context.getString(R.string.download)
            val applink = context.getString(R.string.app_link)
            val messge = String.format(context.getString(R.string.share_info_message),
                    context.getString(R.string.app_name))+"\n ${data.title}\n ${data.redirect_link} \n $download\n $applink"
            navigator?.share(messge)
        logEvent(data?.id,"share")




    }
    fun likeClick(data:Post){

        uiScope?.launch {

           results.value = updateRow(data)
            appDataManager.updateViewCount(data.id)
            logEvent(data?.id,"like")
        }



    }

    private fun updateRow(data: Post):List<Post>{
        val size = mData?.size
            for(i in 0..size){
                var p = mData.get(i)
                if(p.id==data.id){
                    p.likesCount=+1
                    mData[i] = p
                    return mData
                }
            }
        return mData
    }


    private fun logEvent(id:String?,action:String){
        val event = "$${id}_$action"
        val likeButton = "card_$action"
        val str = "app_attribute"
        fbAnalytics.logEvent(event,event,str)
        fbAnalytics.logEvent(likeButton,likeButton,str)


    }
    private fun tapEvent(id:String?){
        val event = "${id}_tap"
        fbAnalytics.logEvent(event,event,"app_attribute")
        fbAnalytics.logEvent("article_appview","article_appview","app_attribute")

    }


}
