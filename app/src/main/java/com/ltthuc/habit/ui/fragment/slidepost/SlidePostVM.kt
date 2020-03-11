package com.ltthuc.habit.ui.fragment.slidepost

import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.data.firebase.FirebaseAnalyticsHelper
import com.ltthuc.habit.util.extension.logEvent
import javax.inject.Inject

class SlidePostVM @Inject constructor(
        connectionManager: BaseConnectionManager
) : BaseViewModel<SlidePostNav,Post>( connectionManager){


    @Inject lateinit var fbAnalytics :FirebaseAnalyticsHelper

    fun postClick(post: Post){
        navigator?.gotoPostDetail(post)
        logEvent(post?.id)
    }
    fun catClick(id:String){
        navigator?.gotoCatDetail(id)

    }


    private fun logEvent(id:String?){
        val event = "popular_$id"
        fbAnalytics.logEvent(event,event,"app_attribute")
        fbAnalytics.logEvent("popular_appview","popular_appview","app_attribute")

    }


}