package com.applifehack.knowledge.data.network.response

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
data class RssCatResp constructor(val feed:String?="",
                                  val thumb_url:String?="",
                                  val title:String?="",
                                  val domain:String?=""):Parcelable

@Parcelize
@IgnoreExtraProperties
data class CatResp constructor(val cat_name:String?="",
                               val cat_thumb_url:String?="",
                               val cat_id:String?="",
                               val cat_desc:String?="",
                               val cat_type:String?=""):Parcelable