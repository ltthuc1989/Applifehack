package com.ezyplanet.supercab.data.local.db


import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.util.PostStatus
import io.reactivex.Observable
import java.util.*

interface DbHelper{


    fun insertFavoritePost(post:Post)

    fun loadFavoritePosts(liked_date:Date?): List<Post>

    fun removeAll():Observable<Int>
    fun getPostById(id:String?) : Post
    fun getPostByAuthor(author:String?) : List<Post>
    suspend fun updatePost(id:String?,status:String? = PostStatus.PUBLISH.type)
    fun insertPost(address: List<Post>)

    fun loadAllPost(): List<Post>
}