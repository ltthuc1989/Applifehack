package com.ltthuc.habit.util

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.ltthuc.habit.R

object AlertDialogUtils{

    fun showSingleChoice(context:Context, @StringRes titleId:Int,@ArrayRes array:Int,action:(String)->Unit={}){

        lateinit var dialog: AlertDialog

        val array = context.resources.getStringArray(array)

        val builder = AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle_Dark)
        builder.setTitle(context.getString(titleId))

        builder.setSingleChoiceItems(array,-1){_,which->
            action(array[which])
            dialog.dismiss()
        }
        dialog = builder.create()

        dialog.show()
    }

    fun showSingleChoice(context:Context,  title:String?,@ArrayRes array:Int,action:(String)->Unit={}){

        lateinit var dialog: AlertDialog

        val array = context.resources.getStringArray(array)

        val builder = AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle_Dark)
        builder.setTitle(title)

        builder.setSingleChoiceItems(array,-1){_,which->
            action(array[which])
            dialog.dismiss()
        }
        dialog = builder.create()

        dialog.show()
    }

    fun showSingleChoice(context:Context, @StringRes titleId:Int,array:Array<String>,action:(String)->Unit={}){

        lateinit var dialog: AlertDialog

        val builder = AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle_Dark)
        builder.setTitle(context.getString(titleId))

        builder.setSingleChoiceItems(array,-1){_,which->
            action(array[which])
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
}