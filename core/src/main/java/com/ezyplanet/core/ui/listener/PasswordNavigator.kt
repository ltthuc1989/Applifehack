package com.ezyplanet.core.ui.listener

import com.ezyplanet.core.ui.base.MvvmNav


interface PasswordNavigator:MvvmNav{

    fun openHomeActivity()
    fun serverLogin()
    fun remeberMe()
    fun forgotPass()
}