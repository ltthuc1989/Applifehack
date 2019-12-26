package com.ltthuc.habit.data.network

import com.androidhuman.rxfirebase2.firestore.model.Value
import com.google.android.gms.tasks.Task
import com.google.api.services.youtube.model.Video
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Transaction
import com.ltthuc.habit.data.entity.Post
import com.ltthuc.habit.data.network.response.youtube.YoutubeResp
import com.ltthuc.habit.util.SortBy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.Deferred


interface ApiHelper {

    fun getApiHeader(): ApiHeader
    fun getRssCat():Single<Value<QuerySnapshot>>
    fun createPost(postContent: Post) : Completable
    fun getPost(loadMore:Boolean?=false,lastItem:DocumentSnapshot?=null):Task<QuerySnapshot>
    fun getCatgories(): Single<Value<QuerySnapshot>>
    fun getPostByCat(catId:String?, sortBy:SortBy?=SortBy.NEWEST, loadMore:Boolean?=false, lastItem:DocumentSnapshot?=null):Task<QuerySnapshot>
    fun getVideoPostByCat(catId:String?,sortBy:SortBy?=SortBy.NEWEST,loadMore:Boolean?=false,lastItem:DocumentSnapshot?=null):Task<QuerySnapshot>
    fun getPostByQuote(typeQuote:String?,sortBy:SortBy?=SortBy.NEWEST,loadMore:Boolean?=false,lastItem:DocumentSnapshot?=null):Task<QuerySnapshot>
    fun getYtDetail(youtubeId:String?):Single<YoutubeResp>
     fun updateViewCount(postId:String?):Task<Transaction>

}