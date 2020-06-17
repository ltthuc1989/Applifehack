package com.applifehack.knowledge.data.local.dao

import androidx.room.*
import com.applifehack.knowledge.data.entity.Post

import com.applifehack.knowledge.util.PostStatus
import java.util.*

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(post: List<Post>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post)



    @Query("SELECT * FROM post where liked_date >:date order by liked_date DESC limit 10")
    fun loadAll(date: Date): List<Post>
    @Query("SELECT * FROM post  order by liked_date DESC limit 10")
    fun loadAll(): List<Post>

    @Query("DELETE FROM post WHERE id =:id")
    fun remove(id: Long)

    @Query("SELECT * FROM post WHERE id =:postId ")
    fun getPostById(postId:String?) : Post

}

