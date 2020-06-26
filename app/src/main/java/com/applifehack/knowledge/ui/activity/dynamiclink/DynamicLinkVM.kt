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
) : BaseViewModel<DynamicLinkNav, Post>(schedulerProvider, connectionManager) {

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


                val post = snapshot.await()

                navigator?.hideProgress()
                navigator?.openFeedFragment(post)

            }
        }

    }

}