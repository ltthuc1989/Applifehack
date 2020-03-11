package com.ltthuc.habit.util

import com.google.gson.Gson

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
}