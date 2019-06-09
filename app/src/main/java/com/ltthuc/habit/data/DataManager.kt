package com.irmansyah.catalogmoviekotlin.data

import com.ltthuc.habit.data.network.ApiHelper


interface DataManager : ApiHelper {
    fun updateApiHeader(userId: Long?, accessToken: String)

    fun setUserAsLoggedOut()




    enum class LoggedInMode private constructor(val type: Int) {
        LOGGED_IN_MODE_LOGGED_OUT(0),
        LOGGED_IN_MODE_GOOGLE(1),
        LOGGED_IN_MODE_FB(2),
        LOGGED_IN_MODE_SERVER(3)
    }



}