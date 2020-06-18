package com.applifehack.knowledge.data.network

import com.androidhuman.rxfirebase2.firestore.model.Value
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Transaction
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.network.response.youtube.YoutubeResp
import com.applifehack.knowledge.util.SortBy
import com.google.firebase.storage.UploadTask
import io.reactivex.Completable
import io.reactivex.Single
import java.io.File


interface ApiHelper {

    //
    fun createPostLive(generateId:String,postContent: Post) : Task<Transaction>
    fun createMultiplePost(posts:List<Post>) : Task<Void>
    fun uploadDatabase(file: File) : UploadTask
    fun downloadDatabase(path:String,action:(File)->Unit)
    fun getHtmlDoc(url:String) :Single<String>
    fun createPost(generateId:String,postContent: Post) : Task<Transaction>
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