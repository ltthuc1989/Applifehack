package com.applifehack.knowledge.util

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.applifehack.knowledge.R
import com.ezyplanet.core.ui.base.MvvmActivity
import java.lang.System.exit

object AlertDialogUtils{

    fun showSingleChoice(context:Context,  title:String?,array:Array<String>,position:Int=0,action:(Int)->Unit={}){

        lateinit var dialog: AlertDialog

        // val array = context.resources.getStringArray(array)

        val builder = AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle_Dark)
      //  builder.setTitle(title)

        builder.setSingleChoiceItems(array,position){_,which->
            action(which)
            dialog.dismiss()
        }
        dialog = builder.create()

        dialog.show()
    }

    fun showSingleChoice(context:Context,  title:String?,@ArrayRes array:Int,position:Int=0,action:(String)->Unit={}){

        lateinit var dialog: AlertDialog

        val array = context.resources.getStringArray(array)

        val builder = AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle_Dark)
        builder.setTitle(title)

        builder.setSingleChoiceItems(array,position){_,which->
            action(array[which])
            dialog.dismiss()
        }
        dialog = builder.create()

        dialog.show()
    }

    fun showSingleChoice(context:Context, @StringRes titleId:Int,array:Array<String>,action:(Int)->Unit={}){

        lateinit var dialog: AlertDialog

        val builder = AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle_Dark)
        builder.setTitle(context.getString(titleId))

        builder.setSingleChoiceItems(array,-1){_,which->
            action(which)
            dialog.dismiss()
        }
        dialog = builder.create()

        dialog.show()
    }

    fun showSingleChoice(context:Context,  title:String,array:Array<String>,action:(String)->Unit={}){

        lateinit var dialog: AlertDialog

        val builder = AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle_Dark)
        builder.setTitle(title)

        builder.setSingleChoiceItems(array,-1){_,which->
            action(array[which])
            dialog.dismiss()
        }
        dialog = builder.create()

        dialog.show()
    }

    fun showInternetAlertDialog(context: Context, title:Int, message: Int,
                                plistener: (DialogInterface)->Unit, nlistener: (DialogInterface)->Unit={}) {
        if ((context as MvvmActivity<*, *>).isFinishing) {
            return
        }
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(context.getString(R.string.try_again)){ dlog, _ ->

            plistener.invoke(dlog)
            dlog.dismiss()
        }
        builder.setNegativeButton(context.getString(R.string.exit)){dlog,_->
            nlistener.invoke(dlog)
            dlog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)

        // alertDialog.setOnDismissListener(listener);

        alertDialog.show()
    }
}