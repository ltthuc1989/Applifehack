package com.applifehack.knowledge.data.local.db



import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.local.dao.PostDao
import com.applifehack.knowledge.util.Converters


@Database(entities = [(Post::class)], version = 4)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}