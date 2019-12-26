package com.ltthuc.habit.data.network.response

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
data class CatResp constructor(val name:String?="",
                                  val image:String?="",
                                  val id:String?="",
                                  val desc:String?="",
                               val type:String?=""):Parcelable