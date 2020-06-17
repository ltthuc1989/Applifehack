package com.applifehack.knowledge.ui.activity.dynamiclink

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import com.applifehack.knowledge.BuildConfig
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.ui.activity.splash.SplashNav
import com.applifehack.knowledge.ui.fragment.feed.FeedNav
import com.applifehack.knowledge.ui.widget.QuoteView
import com.applifehack.knowledge.util.extension.await
import com.applifehack.knowledge.util.extension.logEvent
import com.applifehack.knowledge.util.extension.shareImage
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase
import com.makeramen.roundedimageview.RoundedDrawable
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.sourcei.kowts.utils.functions.F
import org.sourcei.kowts.utils.pojo.QuoteResp
import javax.inject.Inject

class DynamicLinkVM  @Inject constructor(val appDataManager: AppDataManager, schedulerProvider: SchedulerProvider, connectionManager: BaseConnectionManager
) : BaseViewModel<FeedNav, Post>(schedulerProvider, connectionManager) {

    @Inject lateinit var fbAnalytics: FirebaseAnalyticsHelper

    private fun logEvent(id:String?,action:String){
        val event = "$${id}_$action"
        val likeButton = "card_$action"
        val str = "app_attribute"
        fbAnalytics.logEvent(event,event,str)
        fbAnalytics.logEvent(likeButton,likeButton,str)


    }
    fun getPostFromId(id:String?){
        navigator?.showProgress()
        uiScope?.launch {
            val data = appDataManager.getPostDetail(id!!).addOnSuccessListener {

            }.addOnFailureListener {
                Log.d("abc",it.message)
            }
            data?.await().let {

                    val snapshot = async(Dispatchers.Default) {
                        it.toObject(Post::class.java)
                    }


                _model.value = snapshot.await()
                resetLoadingState = false
                navigator?.hideProgress()

            }
        }

    }
    fun openPostDetail(data: Post,shareView: View) {
        uiScope?.launch {
            try {

                if(data?.getPostType()== PostType.VIDEO){
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

    fun likeClick(data:Post){
        val temp = model
        uiScope?.launch {
            _model.value = temp.value?.apply { likesCount=+1 }
            appDataManager.updateLikeCount(data.id)
            logEvent(data?.id,"like")
        }
    }


    fun shareClick(view: View, data:Post){
        if(data.getPostType()==PostType.QUOTE){
            val view = view.rootView.findViewById<QuoteView>(R.id.quoteView)
            val quote = view.getQuote()
            generataQuote(view.context,quote,data.id)

        }else{


            val image = ((view.parent.parent as RelativeLayout).findViewById<RoundedImageView>(R.id.feed_hero_image))
            val bitmap = (image.drawable as RoundedDrawable).sourceBitmap
            generateArticle(view.context,data,bitmap)
        }
        logEvent(data?.id,"share")



    }


    private fun createDynamicLink(context: Context, postId:String?, bitmap: Bitmap){
        val dynamicLink = Firebase.dynamicLinks.shortLinkAsync {
            link = Uri.parse("https://www.applifehack.com/$postId")
            domainUriPrefix = "${BuildConfig.URL_DYNAMIC_LINK}"

            // Open links with this app on Android

        }.addOnSuccessListener {
            ( context as MvvmActivity<*, *>).shareImage(bitmap,it.shortLink.toString())
        }.addOnFailureListener{
            it.printStackTrace()
        }
    }
}