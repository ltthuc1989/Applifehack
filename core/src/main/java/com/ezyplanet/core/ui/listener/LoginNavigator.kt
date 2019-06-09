package com.ezyplanet.core.ui.listener

import com.ezyplanet.core.ui.base.MvvmNav


interface LoginNavigator:MvvmNav{


         fun facebookLoginClick()
         fun phoneLoginClick()
         fun passwordLoginClick()
         fun openHomeActivity()
         fun showLoading()
         fun hideLoading()




}