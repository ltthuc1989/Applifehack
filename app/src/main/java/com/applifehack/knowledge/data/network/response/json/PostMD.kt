package com.applifehack.knowledge.data.network.response.json

import com.google.gson.annotations.SerializedName

data class PostMD(@SerializedName("post_type")
    val postType: List<PostTypeItem>?,
                  val category: List<CategoryItem>?,
                  @SerializedName("quote_type")
                  val quoteType: List<QuoteTypeItem>?)