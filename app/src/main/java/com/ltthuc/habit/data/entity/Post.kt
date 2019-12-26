package com.ltthuc.habit.data.entity

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize
import kotlin.collections.HashMap

@Parcelize
@IgnoreExtraProperties
class Post(var id: String? = "",
           var title: String? = "",
           var description: String? = "",
           var createdDate: Timestamp? = null,
           var likesCount: Long? = 0,
           var imgLink: String? = "",
           var catId: String? = "",
           var contentId: String? = "",
           var webLink: String? = "",
           var video_url: String? = "",
           var redirect_link: String? = "",
           var type: String? = "article",
           var viewsCount: Long? = 0,
           var catName: String? = "",
           var duration: String? = "",
           var author:String?="",
           var authorLink:String?="") : Parcelable {


    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result["title"] = title!!
        result["description"] = description!!

        result["imgLink"] = imgLink!!
        result["webLink"] = webLink!!
        result["url"] = redirect_link!!
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

}

enum class PostType(val type: String?) {
    ARTICLE("article"),
    QUOTE("quote"),
    VIDEO("video")

}