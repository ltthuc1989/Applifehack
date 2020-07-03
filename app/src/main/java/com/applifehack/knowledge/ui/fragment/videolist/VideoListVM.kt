package com.applifehack.knowledge.ui.fragment.videolist

import android.util.Log
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.util.SortBy
import com.applifehack.knowledge.util.extension.await
import com.ezyplanet.supercab.data.local.db.DbHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class VideoListVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider,val dbHelper: DbHelper, connectionManager: BaseConnectionManager
) : BaseViewModel<VideoListNav, String>(schedulerProvider, connectionManager) {

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
                val data = appDataManager.getVideoPostByCat(catId,SortBy.NEWEST, nextPage,lastItem)

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

                        isNoMoreDataLoad = false
                        currentPage += 1
                    }else{
                        isNoMoreDataLoad = true
                    }
                    results.value = myFavoritePost(mData)
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

    fun likeClick(data:Post){
        uiScope?.launch {
            results.value = updateRow(data)
            try {
                appDataManager.updateLikeCount(data.id)
            }catch (ex:Exception){
                ex.printStackTrace()
            }
            withContext(Dispatchers.IO) {
                dbHelper.insertFavoritePost(data.apply {
                    likedDate = Date()
                    liked = true })
                logEvent(data?.id)
            }
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

    fun onItemClicked(item: Post){

        navigator?.openYoutube(item)
        logEvent(item?.id)
    }

    fun onLoadMore(page: Int) {
        if(isNoMoreDataLoad) return
        getPost(catId,true)
    }


    private fun logEvent(id:String?){
        val event = "${id}_tap"
        fbAnalytics.logEvent(event,event,"app_attribute")
        fbAnalytics.logEvent("video_appview","video_appview","app_attribute")

    }
    suspend fun  myFavoritePost(data:List<Post>) = withContext(Dispatchers.Default){
        data.forEach {


            val temp =  async(Dispatchers.IO) {
                dbHelper.getPostById(it.id)
            }.await()
            if(temp!=null){

                it.liked = true
            }

        }
        data

    }
}
