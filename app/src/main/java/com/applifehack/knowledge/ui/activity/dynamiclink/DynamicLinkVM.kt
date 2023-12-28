package com.applifehack.knowledge.ui.activity.dynamiclink

import android.util.Log
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.firebase.FirebaseAnalyticsHelper
import com.applifehack.knowledge.util.extension.await
import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.core.util.SchedulerProvider
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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
                Log.d("abc",it.message!!)
            }
            data?.await().let {

                val snapshot = async(Dispatchers.Default) {
                    it?.toObject(Post::class.java)
                }


                val post = snapshot.await()

                navigator?.hideProgress()
                navigator?.openFeedFragment(post)

            }
        }

    }

}