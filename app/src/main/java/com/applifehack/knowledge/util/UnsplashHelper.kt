package com.applifehack.knowledge.util

import android.net.Uri
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class UnsplashHelper {

    companion object {
        private const val BASE_URL = "https://api.unsplash.com//photos/random?query="
        private const val ACCESS_KEY = "7ncUbNBayqyuAHt9XXsKUiMmpbSOQu6Zv0lcdyJ3H5Q"
        // private const val SECRET_KEY = "966b..."

        // val instance: UnsplashHelper by lazy { UnsplashHelper() }

        // https://unsplash.com/documentation#dynamically-resizable-images
        const val SIZE_SMALL = "fm=webp&w=750&h=500&fit=max"
        const val SIZE_FULL = "fm=webp&w=1600&fit=max"
    }

    fun getImageUrl(url: String, size: String): String {
        return "$url&$size"
    }

    // https://unsplash.com/documentation#get-a-photo
    fun getPhotoUrl(query: String): String {

        val uri = Uri.parse("$BASE_URL&$query$SIZE_SMALL")
            .buildUpon()
            //.appendPath(path)
            .build()

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(uri.toString())
            .addHeader("Accept-Version", "v1")
            .addHeader("Authorization", "Client-ID $ACCESS_KEY")
            .get()
            .build()

        val response = client.newCall(request).execute()
        val jsonDataString = response.body?.string()

        val json = JSONObject(jsonDataString)
        if (!response.isSuccessful) {
            val errors = json.getJSONArray("errors").join(", ")
            throw Exception(errors)
        }
        val rawUrl = json.getJSONObject("urls").getString("custom")
        return rawUrl
    }
}