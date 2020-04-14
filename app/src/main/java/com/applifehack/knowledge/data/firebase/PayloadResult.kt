package com.applifehack.knowledge.data.firebase

import android.os.Parcelable
import com.applifehack.knowledge.data.entity.PostType
import kotlinx.android.parcel.Parcelize

@Parcelize
class PayloadResult(var title:String?=null,var body:String?=null,var link:String?=null,var postType:String?=null):Parcelable{


    fun getPostType():PostType= when(postType){
            "quote"->{
              PostType.QUOTE
            }
            "video"->{
              PostType.VIDEO
            }
            "article"->{
               PostType.ARTICLE
            }else ->PostType.ARTICLE
        }

}