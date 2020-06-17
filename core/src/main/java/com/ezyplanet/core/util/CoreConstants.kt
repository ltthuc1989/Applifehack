package com.ezyplanet.core.util


/**
 * Created by jyotidubey on 05/01/18.
 */
object CoreConstants {
    val API_STATUS_CODE_LOCAL_ERROR = 0
    val LIMIT_ITEM_PER_PAGE=10
    val VISIBLE_THRESHOLD = 5
    val LOGIN_SUCESS="login_success"



     val APP_DB_NAME = "posts.db"
     val PREF_NAME = "mindorks_pref"



    enum class LoggedInMode constructor(val type: Int) {
        LOGGED_IN_MODE_LOGGED_OUT(0),
        LOGGED_IN_MODE_GOOGLE(1),
        LOGGED_IN_MODE_FB(2),
        LOGGED_IN_MODE_SERVER(3)
    }
}