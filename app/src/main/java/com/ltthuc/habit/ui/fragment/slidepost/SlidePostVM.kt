package com.ltthuc.habit.ui.fragment.slidepost

import com.ezyplanet.core.ui.base.BaseViewModel
import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ltthuc.habit.data.entity.Post
import javax.inject.Inject

class SlidePostVM @Inject constructor(
        connectionManager: BaseConnectionManager
) : BaseViewModel<SlidePostNav,Post>( connectionManager){




    fun postClick(post: Post){
       navigator?.gotoPostDetail(post)
    }
    fun catClick(id:String){
        navigator?.gotoCatDetail(id)

    }





}