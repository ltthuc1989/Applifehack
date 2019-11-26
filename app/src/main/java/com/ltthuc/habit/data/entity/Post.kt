package com.ltthuc.habit.data.entity

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.ltthuc.habit.util.FormatterUtil
import kotlinx.android.parcel.Parcelize
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
           var video:String?="",
           var url:String?="",
           var type:String?="",
           var viewsCount:Long?=0,
           var catName:String?=""):Parcelable{
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
}