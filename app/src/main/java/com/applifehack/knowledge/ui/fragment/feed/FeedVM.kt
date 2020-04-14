package com.applifehack.knowledge.ui.fragment.feed

import android.content.Context
import android.util.Log
import android.view.View
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.util.extension.await
import com.applifehack.knowledge.util.extension.shareImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.sourcei.kowts.utils.functions.F
import org.sourcei.kowts.utils.pojo.QuoteResp
import java.util.ArrayList
import javax.inject.Inject

class FeedVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<FeedNav, String>(schedulerProvider, connectionManager) {

    val results = NonNullLiveData<List<Post>>(emptyList())
    private val mData = ArrayList<Post>()

    private var lastItem: DocumentSnapshot?=null
     var currentPage=0
    init {
        visibleThreshold = 10
    }

    private var hasMore = false
    private var currentItem = 0
    @Inject lateinit var fbAnalytics: FirebaseAnalyticsHelper

    fun getPost(nextPage: Boolean? = false) {

        if (nextPage == false) navigator?.showProgress()
        resetLoadingState = true
        uiScope?.launch {
            try {
                val data = appDataManager.getPost( nextPage,lastItem)

                data?.await().let {
                    if(!it.isEmpty) {
                        val snapshot = async(Dispatchers.Default) {
                            it.toObjects(Post::class.java)
                        }


                        lastItem = it.documents[it.size() - 1]
                        val rs = snapshot.await()
                        mData.addAll(rs)
                        hasMore=(rs.size>visibleThreshold-1)




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

    fun openPostDetail(data: Post,shareView:View) {
        uiScope?.launch {
            try {

                if(data?.getPostType()==PostType.VIDEO){
                    navigator?.gotoYoutubeDetail(data,shareView)

                }else{
                    navigator?.gotoFeedDetail(data)
                }
                logEvent(data?.id,"readmore")

                val resul = appDataManager.updateViewCount(data.id)
                resul.await()

            } catch (ex: Exception) {

            }


        }

    }
    fun openCatDetail(data: Post){
        navigator?.gotoCatDetail(data?.catId)
    }
    fun openPageUrl(data: Post){
        navigator?.gotoPageUrl(data)
    }
    fun shareClick(view: View, data:Post){
        if(data.getPostType()==PostType.QUOTE){
            navigator?.shareImage(view)

        }else{
            val context = view.context
            val download = context.getString(R.string.download)
            val applink = context.getString(R.string.app_link)
            val messge = String.format(context.getString(R.string.share_info_message),
                    context.getString(R.string.app_name))+"\n ${data.title}\n ${data.redirect_link} \n $download\n $applink"
            navigator?.share(messge)
        }
        logEvent(data?.id,"share")



    }
    fun likeClick(data:Post){
        uiScope?.launch {
            results.value = updateRow(currentItem)
            appDataManager.updateLikeCount(data.id)
            logEvent(data?.id,"like")
        }
    }



    fun onLoadMore(position: Int) {
        currentItem = position
        if(hasMore&&position+5>mData.size){
            getPost(true)
        }

    }

    fun generataQuote(context: Context,quoteResp: QuoteResp){
        uiScope?.launch {
            try {
                navigator?.showProgress()

                val result = F.generateBitmap(context, quoteResp)
                navigator?.hideProgress()
                ( context as MvvmActivity<*, *>).shareImage(result)
            }catch (ex:Exception){
                ex.printStackTrace()
            }


        }


    }

    private fun updateRow(position: Int):List<Post>{

       mData[position]?.apply {
           likesCount =likesCount!!+1
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


}