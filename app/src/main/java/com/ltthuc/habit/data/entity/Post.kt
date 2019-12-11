package com.ltthuc.habit.data.entity

import android.os.Parcelable
import android.text.format.DateUtils
import com.google.firebase.database.IgnoreExtraProperties
import com.ltthuc.habit.util.FormatterUtil
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap
@Parcelize
@IgnoreExtraProperties
class Post(var id:String?="",
           var title:String?="",
           var description:String?="",
           var createdDate:Long?=0,
           var likesCount:Long?=0,
           var imgLink:String?="",
           var catId:String?="",
           var contentId:String?="",
           var webLink:String?="",
           var video_url:String?="",
           var url:String?="",
           var type:String?="article",
           var viewsCount:Long?=0,
           var catName:String?="",
           var duration:String?=""):Parcelable{
    init {
        createdDate = Date().time
    }

    fun toMap():Map<String,Any>{
        val result = HashMap<String,Any>()
        result["title"]= title!!
        result["description"] = description!!
        result["createdDateText"] =FormatterUtil.getFirebaseDateFormat()
            .format(Date(createdDate!!))
        result["imgLink"] = imgLink!!
        result["webLink"] = webLink!!
        result["url"] = url!!
        return result

    }
    fun getPostType():PostType=when(type){
            "article"-> PostType.ARTICLE
            else ->  PostType.VIDEO

    }

}
enum class PostType(val type: String?){
    ARTICLE("article"),
    VIDEO("video")
}