package com.ezyplanet.supercab.data.local.db

import com.applifehack.knowledge.data.entity.Post

import com.applifehack.knowledge.data.local.dao.PostDao
import com.applifehack.knowledge.util.PostStatus
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppDbHelper @Inject constructor( val addressesDao: PostDao) : DbHelper {


    override fun insertFavoritePost(post: Post) {


            addressesDao.insert(post)


    }

    override fun loadFavoritePosts(liked_date:Date?): List<Post>{

           return if(liked_date==null) addressesDao.loadAll() else addressesDao.loadAll(liked_date)

    }

    override fun removeAll(): Observable<Int> {
        TODO("Not yet implemented")
    }

    override fun getPostById(id: String?): Post {

          return addressesDao.getPostById(id)

    }



}