package com.applifehack.knowledge.data.network.response

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
data class RssCatResp constructor(val feed:String?="",
                                  val thumb_url:String?="",
                                  val title:String?="",
                                  val domain:String?="",
var author_name:String=""):Parcelable

@Parcelize
@IgnoreExtraProperties
data class CatResp constructor(val cat_name:String?="",
                               val cat_thumb_url:String?="",
                               val cat_id:String?="",
                               val cat_desc:String?="",
                               val cat_type:String?="",
                               val cat_post_type:String?="",
                               val editing:Boolean = false):Parcelable

@Parcelize
@IgnoreExtraProperties
data class QuoteResp constructor(val quote_name:String?="",var quote_id:String?=""):Parcelable{

    override fun toString(): String {
        return quote_name!!
    }
}

@Parcelize
@IgnoreExtraProperties
data class FactResp constructor(val fact_name:String?="",var fact_id:String?=""):Parcelable{

    override fun toString(): String {
        return fact_name!!
    }

}

@Parcelize
@IgnoreExtraProperties
data class HackResp constructor(val hack_name:String?="",var hack_id:String?=""):Parcelable{

    override fun toString(): String {
        return hack_name!!
    }
}

@Parcelize
@IgnoreExtraProperties
data class MeaningPhotoResp constructor(val photo_name:String?="",var photo_id:String?=""):Parcelable{

    override fun toString(): String {
        return photo_name!!
    }
}