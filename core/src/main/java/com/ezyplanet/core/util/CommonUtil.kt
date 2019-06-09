package com.ezyplanet.core.util

import android.util.Patterns

import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.ui.widget.TransparentProgressDialog
import android.text.TextUtils



/**
 * Created by jyotidubey on 11/01/18.
 */
object CommonUtil {

    fun showLoadingDialog(baseActivity: MvvmActivity<*, *>): TransparentProgressDialog {
        val display = baseActivity.getWindowManager().getDefaultDisplay()
        val progressDialog = TransparentProgressDialog(baseActivity, display)
        progressDialog.let {
            it.show()
            return it
        }
    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()

    }
     fun isValidPhoneNumber(phoneNumber: String): Boolean {

        return !TextUtils.isEmpty(phoneNumber) && Patterns.PHONE.matcher(phoneNumber).matches()
    }


}