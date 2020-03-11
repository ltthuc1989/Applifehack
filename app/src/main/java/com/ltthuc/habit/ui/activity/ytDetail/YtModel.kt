package com.ltthuc.habit.ui.activity.ytDetail

import android.content.Context
import com.google.api.services.youtube.model.Video
import com.ltthuc.habit.R
import com.ltthuc.habit.data.network.response.youtube.Item
import com.ltthuc.habit.util.TimeUtil

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