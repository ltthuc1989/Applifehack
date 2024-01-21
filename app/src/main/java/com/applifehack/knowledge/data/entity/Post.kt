package com.applifehack.knowledge.data.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.HashMap
import java.util.regex.Pattern


@Parcelize
@Entity
data class Post(
    @set:PropertyName("post_id")
    @get:PropertyName("post_id")
    @PrimaryKey
    @NonNull
    var id: String ="post",
    @set:PropertyName("post_title")
    @get:PropertyName("post_title")
    @ColumnInfo(name = "post_title")
    var title: String? = "",

    @set:PropertyName("post_desc")
    @get:PropertyName("post_desc")
    @ColumnInfo(name = "post_desc")
    var description: String? = "",

    @set:PropertyName("post_created_date")
    @get:PropertyName("post_created_date")
    @ColumnInfo(name = "post_created_date")
    var createdDate: Date? = null,

    @set:PropertyName("post_like_count")
    @get:PropertyName("post_like_count")
    @ColumnInfo(name = "post_like_count")
    var likesCount: Int? = 0,

    @set:PropertyName("post_image_url")
    @get:PropertyName("post_image_url")
    @ColumnInfo(name = "post_image_url")
    var imageUrl: String? = "",

    @set:PropertyName("post_cat_id")
    @get:PropertyName("post_cat_id")
    @ColumnInfo(name = "post_cat_id")
    var catId: String? = "",


    @set:PropertyName("post_video_id")
    @get:PropertyName("post_video_id")
    @ColumnInfo(name = "post_video_id")
    var video_url: String? = "",

    @set:PropertyName("post_content_url")
    @get:PropertyName("post_content_url")
    @ColumnInfo(name = "post_content_url")
    var redirect_link: String? = "",

    @set:PropertyName("post_type")
    @get:PropertyName("post_type")
    @ColumnInfo(name = "post_type")
    var type: String? = "article",

    @set:PropertyName("post_view_count")
    @get:PropertyName("post_view_count")
    @ColumnInfo(name = "post_view_count")
    var viewsCount: Long? = 0,

    @set:PropertyName("post_cat_name")
    @get:PropertyName("post_cat_name")
    @ColumnInfo(name = "post_cat_name")
    var catName: String? = "",

    @set:PropertyName("post_video_duration")
    @get:PropertyName("post_video_duration")
    @ColumnInfo(name = "post_video_duration")
    var duration: String? = "",

    @set:PropertyName("post_author_name")
    @get:PropertyName("post_author_name")
    @ColumnInfo(name = "post_author_name")
    var author: String? = "",

    @set:PropertyName("post_author_url")
    @get:PropertyName("post_author_url")
    @ColumnInfo(name = "post_author_url")
    var authorUrl: String? = "",
    @set:PropertyName("post_quote_type")
    @get:PropertyName("post_quote_type")
    @ColumnInfo(name = "post_quote_type")
    var quote_type: String? = "",
    @ColumnInfo(name = "post_parsed_date")
    var parsed_date:String?="",
    @ColumnInfo(name = "status")
    var post_status :String?="parsed",
    @ColumnInfo(name = "viewCount")
    var viewCount :Int?=0,
    @set:PropertyName("liked")
    @get:PropertyName("liked")
    @ColumnInfo(name = "liked")
    var liked :Boolean?=false,
    @set:PropertyName("liked_date")
    @get:PropertyName("liked_date")
    @ColumnInfo(name = "liked_date")
    var likedDate: Date?= Date(),
    @set:PropertyName("post_video_views")
    @get:PropertyName("post_video_views")
    @ColumnInfo(name = "post_video_views")
    var video_views : String? ="",
    @set:PropertyName("post_author_type")
    @get:PropertyName("post_author_type")
    @ColumnInfo(name = "post_author_type")
    var author_type : String? ="",

    @set:PropertyName("post_image_caption")
    @get:PropertyName("post_image_caption")
    @ColumnInfo(name = "post_image_caption")
    var image_caption: String? = "",

    @set:PropertyName("post_send_push")
    @get:PropertyName("post_send_push")
    @ColumnInfo(name = "post_send_push")
    var send_push: Int? = null,

    @set:PropertyName("post_push_style")
    @get:PropertyName("post_push_style")
    @ColumnInfo(name = "post_push_style")
    var push_style: Int? = null
) : Parcelable {


    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result["post_title"] = title!!
        result["post_desc"] = description!!

        result["post_image_url"] = imageUrl!!
        result["post_content_url"] = redirect_link!!
        result["post_cat_name"] = catName!!
        result["post_cat_id"] = catId!!
        result["post_author_name"] = author!!
        result["post_author_url"] = authorUrl!!
        result["post_created_date"] = Timestamp.now()
        result["post_id"] = id!!
        result["post_type"] = type!!
        result["post_image_caption"] = image_caption?: ""
        if(quote_type?.isEmpty()!=true){
            result["post_quote_type"] = quote_type!!
        }
        send_push?.let {
            result["post_send_push"] = 1
            if (push_style == null || push_style == 0) {
                // notification normal
                result["post_push_style"] = 0
            } else {
                // notification image style
                result["post_push_style"] = 1
            }
        }
        if(type == PostType.VIDEO.type){
            result["post_video_id"] = video_url!!
            duration?.let {
                result["post_video_duration"] = it
            }
            video_views?.let {
                result["post_video_views"] = it
            }

        }

        return result

    }

    fun getPostType(): PostType = when (type) {
        "article" -> PostType.ARTICLE
        "quote" -> PostType.QUOTE
        else -> PostType.VIDEO

    }

    fun getPublishedDate(): String? {
        return ""
    }

    fun getVideoId(): String? {
        return video_url

//        val pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
//
//        val compiledPattern = Pattern.compile(pattern)
//        val matcher = compiledPattern.matcher(video_url)
//
//        if (matcher.find()) {
//            return matcher.group()
//        }
//        return ""
    }

    fun formatLikes(): String = "$likesCount"
    fun formatViews():String = "$video_views"
    fun quoteImage():String{
        return "https://source.unsplash.com/750x500/?$quote_type"
    }

    fun getVideoViews():String{
        if(video_views?.isEmpty()==true){
            return ""
        }else{
            return "  $video_views views"
        }
    }

    override fun toString(): String {
        return "${id}, $title"
    }

}

enum class PostType(val type: String?) {
    ARTICLE("article"),
    QUOTE("quote"),
    VIDEO("video")

}

