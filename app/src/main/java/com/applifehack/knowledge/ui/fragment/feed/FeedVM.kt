package com.applifehack.knowledge.ui.fragment.feed

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.applifehack.knowledge.BuildConfig
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.ui.widget.QuoteView
import com.applifehack.knowledge.util.extension.await
import com.applifehack.knowledge.util.extension.shareImage
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.supercab.data.local.db.DbHelper
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.livedata.NonNullLiveData
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase
import com.makeramen.roundedimageview.RoundedDrawable
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.sourcei.kowts.utils.functions.F
import org.sourcei.kowts.utils.pojo.QuoteResp
import java.util.*
import javax.inject.Inject


open class FeedVM @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, var dbHelper: DbHelper, connectionManager: BaseConnectionManager
) : BaseViewModel<FeedNav, String>(schedulerProvider, connectionManager) {

    val results = NonNullLiveData<List<Post>>(emptyList())
    protected val mData = ArrayList<Post>()

    private var lastItem: DocumentSnapshot?=null
     var currentPage=0
    init {
        visibleThreshold = 10
    }

    protected var hasMore = false
    protected var currentItem = 0
    @Inject lateinit var fbAnalytics: FirebaseAnalyticsHelper

   open fun getPost(nextPage: Boolean? = false) {

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
            val view = view.rootView.findViewById<QuoteView>(R.id.quoteView)
            val quote = view.getQuote()
            generataQuote(view.context,quote,data.id)

        }else{
//

             val image = ((view.parent.parent as RelativeLayout).findViewById<RoundedImageView>(R.id.feed_hero_image))
            val bitmap = (image.drawable as RoundedDrawable).sourceBitmap
            generateArticle(view.context,data,bitmap)
        }
        logEvent(data?.id,"share")



    }
    fun likeClick(data:Post){
        uiScope?.launch {
            results.value = updateRow(currentItem)
            appDataManager.updateLikeCount(data.id)
            withContext(Dispatchers.IO) {
                dbHelper.insertFavoritePost(data.apply {
                    likedDate = Date()
                    liked = true })
                logEvent(data?.id, "like")
            }
        }
    }



    fun onLoadMore(position: Int) {
        currentItem = position
        if(hasMore&&position+5>mData.size){
            getPost(true)
        }

    }

    fun generataQuote(context: Context,quoteResp: QuoteResp,postId: String?){
        uiScope?.launch {
            try {
                navigator?.showProgress()

                val result = F.generateBitmap(context, quoteResp)
                createDynamicLink(context,postId,result!!)
            }catch (ex:Exception){
                ex.printStackTrace()
            }


        }


    }
    fun generateArticle(context: Context,post: Post,bitmap:Bitmap){
        uiScope?.launch {
            try {
                navigator?.showProgress()

                val result = F.createBitmapFromView(context,post,bitmap)

                createDynamicLink(context,post.id!!,result!!)

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

    private fun createDynamicLink(context: Context,postId:String?,bitmap:Bitmap){
        Firebase.dynamicLinks.shortLinkAsync(ShortDynamicLink.Suffix.SHORT) {
            link = Uri.parse("https://www.applifehack.com/?postId=$postId")
            domainUriPrefix = "${BuildConfig.URL_DYNAMIC_LINK}"
            androidParameters("com.applifehack.knowledge.staging"){

            }

            // Open links with this app on Android

        }.addOnSuccessListener {
            navigator?.hideProgress()
            ( context as MvvmActivity<*, *>).shareImage(bitmap,it.shortLink.toString())
        }.addOnFailureListener{
            navigator?.hideProgress()
            it.printStackTrace()
        }
    }
    open fun myFavoritePost(position:Int){
        uiScope?.launch {
          val temp =  async(Dispatchers.IO) {
                dbHelper.getPostById(mData[position].id)
            }.await()
            if(temp!=null){
                results.value = mData?.apply {
                     get(position).liked = true
                }
            }

        }
    }


}