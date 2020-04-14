package com.applifehack.knowledge.util

import android.content.Context
import com.google.gson.Gson
import java.io.IOException

object JsonHelper{

    private fun toHasMap(s: String?): Map<String, String> {
        val temp = s?.replace(Regex("""[\\\s+{}]"""), "")
        val map = temp?.split(",")?.associate {
            val (left, right) = it.split("=")
            left to right
        }
        return map!!
    }

    fun hasMapToJson(s: String?): String {
        val map = toHasMap(s)
        return Gson().toJson(map)

    }

    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}