package com.applifehack.knowledge.data.network

import com.androidhuman.rxfirebase2.firestore.model.Value
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Transaction
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.network.response.youtube.YoutubeResp
import com.applifehack.knowledge.util.SortBy
import io.reactivex.Completable
import io.reactivex.Single


interface ApiHelper {

    fun getApiHeader(): ApiHeader
    fun getRssCat():Single<Value<QuerySnapshot>>
    fun getPost(loadMore:Boolean?=false,lastItem:DocumentSnapshot?=null):Task<QuerySnapshot>
    fun getCatgories(): Single<Value<QuerySnapshot>>
    fun getPostByCat(catId:String?, sortBy:SortBy?=SortBy.NEWEST, loadMore:Boolean?=false, lastItem:DocumentSnapshot?=null):Task<QuerySnapshot>
    fun getPopularPost():Task<QuerySnapshot>
    fun getPostDetail(postId:String):Task<DocumentSnapshot>

    fun getVideoPostByCat(catId:String?,sortBy:SortBy?=SortBy.NEWEST,loadMore:Boolean?=false,lastItem:DocumentSnapshot?=null):Task<QuerySnapshot>
    fun getPostByQuote(typeQuote:String?,loadMore:Boolean?=false,lastItem:DocumentSnapshot?=null):Task<QuerySnapshot>
    fun getYtDetail(youtubeId:String?):Single<YoutubeResp>
     fun updateViewCount(postId:String?):Task<Transaction>
    fun updateLikeCount(postId:String?):Task<Transaction>




}