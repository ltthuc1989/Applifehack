package com.applifehack.knowledge.util

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.applifehack.knowledge.R
import com.ezyplanet.thousandhands.shipper.data.preferences.AppPreferenceHelper

object RateUs {

    private var mDialog: Dialog? = null


    fun rate(activity: Activity, appPreferenceHelper: AppPreferenceHelper) {
        mDialog = Dialog(activity)
        mDialog!!.setContentView(R.layout.alert_dialog_rating)
        val ratingBar = mDialog?.findViewById<View>(R.id.rate_bar) as RatingBar
        val textView = this.mDialog?.findViewById<View>(R.id.not_now) as TextView

        this.mDialog?.show()
        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (rating < 4.0f) {
                mDialog?.dismiss()
                appPreferenceHelper.appLaunchCount = 0
                Toast.makeText(
                    activity.applicationContext,
                    "Thank you for your feedback. We will work on improvements.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnRatingBarChangeListener
            }
            android.os.Handler().postDelayed({
                showAlertRate(activity, appPreferenceHelper)
            }, 500)
        }
        textView.setOnClickListener {
            mDialog?.dismiss()
            appPreferenceHelper.appLaunchCount = 0

        }
        this.mDialog!!.setOnCancelListener {
            mDialog?.dismiss()
            appPreferenceHelper.appLaunchCount = 0
        }
    }

    private fun showAlertRate(activity: Activity, appPreferenceHelper: AppPreferenceHelper) {
        this.mDialog?.dismiss()
        val builder = AlertDialog.Builder(activity)
        val inflate = activity.layoutInflater.inflate(R.layout.alertbox_custom, null)
        builder.setView(inflate)
        val textView = inflate.findViewById(R.id.dialog_positive_btn) as TextView
        val textView2 = inflate.findViewById(R.id.dialog_neutral_btn) as TextView
        val create = builder.create()
        textView.setOnClickListener {

            try {
                activity.startActivity(
                    Intent(
                        "android.intent.action.VIEW",
                        Uri.parse(
                            "market://details?id=" + activity.applicationContext
                                .packageName
                        )
                    )
                )
            } catch (unused: ActivityNotFoundException) {
                Toast.makeText(
                    activity.applicationContext,
                    " unable to find market app",
                    Toast.LENGTH_LONG
                ).show()
            }
            create.dismiss()


        }
        textView2.setOnClickListener {
            create.dismiss()
            appPreferenceHelper.appLaunchCount = 0

        }
        create.show()
    }
}