package com.ltthuc.habit.data.network.response.youtube



data class Item(
        val etag: String,
        val id: String,
        val kind: String,
        val snippet: Snippet,
        val statistics:Statistics
)