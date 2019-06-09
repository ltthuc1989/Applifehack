package com.ezyplanet.core.ui.base


import androidx.annotation.StringRes
import com.ezyplanet.core.ui.listener.RetryCallback

interface MvvmNav {
    fun connectionFail(restId:Int, retryCallback: RetryCallback<*>)
    fun connectionFail(restId:Int)
    fun openActivityOnTokenExpire()
    fun showAlert(message: String?)
    fun showAlert(@StringRes resId: Int, statusCode : Int)
    fun showAlert(@StringRes resId: Int)
    fun showAlert(title: String?,message: String?)
    fun showErrorSnackBar(@StringRes resId: Int)
    fun hideProgress()
    fun showProgress()
    fun onBackPress(){}
    fun showToast(message: String) {

    }
    fun showToast(@StringRes resId: Int) {

    }
    fun showAlertTimeOut(@StringRes resId: Int){

    }

}