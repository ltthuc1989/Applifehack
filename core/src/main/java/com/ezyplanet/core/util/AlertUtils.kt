package com.ezyplanet.core.util


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log

import com.ezyplanet.core.R
import android.widget.LinearLayout
import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.ui.listener.RetryCallback
import java.lang.System.exit


/**
 * Created by misotomovski on 2/5/16.
 */
class AlertUtils {

    companion object {


        fun showOkAlert(context: Context, title: String, message: String) {
            if ((context as MvvmActivity<*, *>).isFinishing) {
                return
            }

            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(context.getString(R.string.OK)) { dlog, _ -> dlog.dismiss() }
            val alertDialog = builder.create()


            alertDialog.show()


        }


        fun showOkAlert(context: Context, title: String, message: String, callback: (DialogInterface) -> Unit = {}) {
            if ((context as MvvmActivity<*, *>).isFinishing) {
                return
            }

            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(context.getString(R.string.OK)) { dlog, _ -> dlog.dismiss() }
            val alertDialog = builder.create()
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.setOnDismissListener(callback)

            alertDialog.show()


        }

        fun showOkAlert(context: Context, title: String, message: String, listener: DialogInterface.OnDismissListener, isCancel: Boolean) {
            if ((context as MvvmActivity<*, *>).isFinishing) {
                return
            }

            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(context.getString(R.string.OK)) { dlog, _ -> dlog.dismiss() }
            val alertDialog = builder.create()
            alertDialog.setCanceledOnTouchOutside(isCancel)
            alertDialog.setOnDismissListener(listener)

            alertDialog.show()


        }




        fun showOkAlertDialog(context: Context, resId: Int) {
            if ((context as MvvmActivity<*, *>).isFinishing) {
                return
            }
            showOkAlertDialog(context, context.getString(resId))
        }

        fun showOkAlertDialog(context: Context, message: String, listener: ()->Unit={}) {
            if ((context as MvvmActivity<*, *>).isFinishing) {
                return
            }
            val builder = AlertDialog.Builder(context)
            builder.setMessage(message)
            builder.setPositiveButton(context.getString(R.string.OK)) { dlog, _ ->
                dlog.dismiss()
                listener.invoke()
            }
            val alertDialog = builder.create()
            alertDialog.setCanceledOnTouchOutside(false)

            alertDialog.show()
        }



        fun showOkAlertDialog(context: Context, message: Int, listener: () -> Unit) {

            if ((context as MvvmActivity<*, *>).isFinishing) {
                return
            }

            val builder = AlertDialog.Builder(context)
            builder.setMessage(context.getString(message))
            builder.setPositiveButton(context.getString(R.string.OK)){d,_->
               listener.invoke()
                d.dismiss()
            }


            val alertDialog = builder.create()
            alertDialog.setCanceledOnTouchOutside(false)
            // alertDialog.setOnDismissListener(listener);

            alertDialog.show()
        }



        fun showYesNoAlertDialog(context: Context, message: String?,
                                 plistener: (DialogInterface)->Unit, nlistener: (DialogInterface)->Unit={}) {
            if ((context as MvvmActivity<*, *>).isFinishing) {
                return
            }
            val builder = AlertDialog.Builder(context)
            builder.setMessage(message)
            builder.setPositiveButton(context.getString(R.string.yes)){ dlog, _ ->

                plistener.invoke(dlog)
                dlog.dismiss()
            }
            builder.setNegativeButton(context.getString(R.string.no)){dlog,_->
                nlistener.invoke(dlog)
                dlog.dismiss()
            }
            val alertDialog = builder.create()
            alertDialog.setCanceledOnTouchOutside(false)

            // alertDialog.setOnDismissListener(listener);

            alertDialog.show()
        }


        fun showCancelRetryAlertDlg(context: Context, message: String,
                                    callback: RetryCallback<*>, pListener: (DialogInterface)->Unit, nListener: (DialogInterface)->Unit) {
            if ((context as MvvmActivity<*, *>).isFinishing) {
                return
            }
            val builder = android.app.AlertDialog.Builder(context)
            builder.setMessage(message)
            builder.setPositiveButton(context.getString(R.string.retry)){dlog,_->
                pListener.invoke(dlog)
                dlog.dismiss()
            }
            builder.setNegativeButton(context.getString(R.string.cancel)){dlog,_->
                nListener.invoke(dlog)
                dlog.dismiss()
            }
            val alertDialog = builder.create()
            alertDialog.setCanceledOnTouchOutside(false)

            alertDialog.show()
        }

    }
}
