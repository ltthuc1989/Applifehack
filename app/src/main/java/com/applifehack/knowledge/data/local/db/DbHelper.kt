package com.ezyplanet.supercab.data.local.db


import com.applifehack.knowledge.data.entity.Post
import io.reactivex.Observable
import java.util.*

interface DbHelper{


    fun insertFavoritePost(post:Post)

    fun loadFavoritePosts(liked_date:Date?): List<Post>

    fun removeAll():Observable<Int>
    fun getPostById(id:String?) : Post


}