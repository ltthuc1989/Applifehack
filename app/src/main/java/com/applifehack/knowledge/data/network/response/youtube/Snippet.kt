package com.applifehack.knowledge.data.network.response.youtube

import java.util.*

data class Snippet(
        val channelId: String,
        val channelTitle: String,
        val description: String,
        val localized: Localized,
        val publishedAt: Date,
        val thumbnails: Thumbnails,
        val title: String
)