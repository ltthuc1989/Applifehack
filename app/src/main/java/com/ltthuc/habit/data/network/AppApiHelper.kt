package com.ltthuc.habit.data.network

import javax.inject.Inject

/**
 * Created by jyotidubey on 04/01/18.
 */
class AppApiHelper @Inject constructor(private val apiHeader: ApiHeader) : ApiHelper {

    val limitPage: Int = 20

    override fun getApiHeader(): ApiHeader {
        return apiHeader
    }
}