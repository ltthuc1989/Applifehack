package com.applifehack.knowledge.ui.activity.ytDetail

import android.content.Context
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.network.response.youtube.Item
import com.applifehack.knowledge.util.TimeUtil

class YtModel(val id:String?,val video: Item){


    fun getPublishedAt(context: Context):String{
        return String.format(context.getString(R.string.publish_on), TimeUtil.format(video?.snippet?.publishedAt))
    }
    fun desc() :String?{
        return video?.snippet?.description
    }
    fun thumnail():String?{
        val temp=video?.snippet?.thumbnails?.medium?.url
       return if(temp!=null) temp else video?.snippet?.thumbnails?.high?.url


    }
    fun title():String?{
        return video?.snippet?.title
    }
    fun disLikeCount():String?{
        return video?.statistics?.dislikeCount
    }
    fun likeCount():String?{
        return video?.statistics.likeCount
    }
    fun viewCount():String?{
        return  video?.statistics?.viewCount
    }


}