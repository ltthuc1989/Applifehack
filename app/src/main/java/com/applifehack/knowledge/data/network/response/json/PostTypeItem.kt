package com.applifehack.knowledge.data.network.response.json

import com.applifehack.knowledge.data.entity.PostType

data class PostTypeItem(val name: String = ""){

    fun postType():PostType=when(name){
            PostType.ARTICLE.type-> PostType.ARTICLE
            PostType.VIDEO.type->PostType.VIDEO
            else->PostType.QUOTE
        }

}