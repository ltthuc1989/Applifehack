package com.ltthuc.habit.ui.fragment.feed

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.ltthuc.habit.R
import com.ltthuc.habit.data.AppDataManager
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.data.entity.PostType
import com.ltthuc.habit.util.extension.await
import com.ltthuc.habit.util.extension.shareImage
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
    private var currentPage=0
    init {
        visibleThreshold = 10
    }

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

    fun openPostDetail(data: Post,shareView:View) {
        uiScope?.launch {
            try {
                if(data?.getPostType()==PostType.VIDEO){
                    navigator?.gotoYoutubeDetail(data,shareView)

                }else{
                    navigator?.gotoFeedDetail(data)
                }

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



    }
    fun likeClick(data:Post){

    }



    fun onLoadMore(page: Int) {
        if(page==1) return
        getPost(true)
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


}