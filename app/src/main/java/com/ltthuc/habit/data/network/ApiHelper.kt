package com.ltthuc.habit.data.network

import com.androidhuman.rxfirebase2.firestore.model.Value
import com.google.api.services.youtube.model.Video
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.data.network.response.youtube.YoutubeResp
import com.ltthuc.habit.ui.activity.listpost.PostContent
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.Deferred


interface ApiHelper {

    fun getApiHeader(): ApiHeader
    fun getRssCat():Single<Value<QuerySnapshot>>
    fun createPost(postContent: Post) : Completable
    fun getPost():Single<Value<QuerySnapshot>>
    fun getCatgories(): Single<Value<QuerySnapshot>>
    fun getPostByCat(catId:String?):Single<Value<QuerySnapshot>>
    fun getVideoPostByCat(catId:String?):Single<Value<QuerySnapshot>>
     fun getYtDetail(youtubeId:String?):Single<YoutubeResp>

}