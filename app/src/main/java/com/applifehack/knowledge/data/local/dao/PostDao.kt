package com.applifehack.knowledge.data.local.dao

import androidx.room.*
import com.applifehack.knowledge.data.entity.Post

import com.applifehack.knowledge.util.PostStatus
import java.util.*

@Dao
interface PostDao {

    @Query("SELECT * FROM post WHERE status =:status ")
    fun loadAll(status:String?): List<Post>

    @Query("SELECT * FROM post WHERE post_author_name =:author AND status =:status   limit 1 ")
    fun getPostByAuthor(author:String?,status :String? = PostStatus.PARSED.type) : List<Post>
    @Query("UPDATE post SET status=:status Where id=:id")
    fun updatePost(id:String?,status: String?)
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

