/**
 * ISC License
 *
 * Copyright 2018-2019, Saksham (DawnImpulse)
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
 * WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
 * OR PERFORMANCE OF THIS SOFTWARE.
 **/
package com.applifehack.knowledge.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.core.view.setMargins
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.entity.Post

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import com.applifehack.knowledge.util.quote.ObjectGradient
import org.sourcei.kowts.utils.pojo.QuoteResp
import org.sourcei.kowts.utils.reusables.Gradients
import kotlin.random.Random


/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-08-20 by Saksham
 * @note Updates :
 * Saksham - 2019 09 06 - master - compare bitmap
 * Saksham - 2019 09 07 - master - handing of multiple quotes
 * Saksham - 2019 09 11 - master - generate quote bitmap for storage
 * Saksham - 2019 09 13 - master - generate quote alignment
 * Saksham - 2019 09 18 - master - shortid & gradients
 * Saksham - 2019 09 19 - master - add icon to generated image
 */
object F {

    fun viewToBitmap(view: View): Bitmap? {
        val createBitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        view.draw(Canvas(createBitmap))
        return createBitmap
    }
    // generate bitmap from view
    fun getBitmapFromView(view: View): Bitmap {

        val bitmap =
            Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.draw(canvas)
        return bitmap
    }


    // Generating random color
    private fun randomColor(): String {
        val chars =
            listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")
        var color = "#"
        for (i in 1..6) {
            color += chars[Math.floor(Math.random() * chars.size).toInt()]
        }
        return color
    }

    // Generate random gradient
    fun randomGradient(): List<Int> {

        return try {
            Gradients.random().colors.map { it.toColorInt() }
        } catch (e: Exception) {

            e.printStackTrace()
            randomGradient()
        }
    }

    // convert dp - px
    fun dpToPx(dp: Int, context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    // get display height
    fun displayDimensions(context: Context): Point {
        val point = Point()
        val mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = mWindowManager.defaultDisplay
        display.getSize(point) //The point now has display dimens
        return point
    }

    // verify two bitmaps
    fun compareBitmaps(b1: Bitmap?, b2: Bitmap?, callback: (Boolean) -> Unit) {

        if (b1 == null || b2 == null) {
            callback(false)
        } else
            GlobalScope.launch {
                try {
                    callback(b1.sameAs(b2)) // callback with compare
                } catch (e: Exception) {

                    e.printStackTrace()
                    callback(false)
                }
            }
    }

    // get quote


    // generate quote bitmap


    // generate shortid
    fun shortid(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..10)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    // read gradients
    fun readGradients(context: Context): List<ObjectGradient> {
        val string = context.resources.openRawResource(com.applifehack.knowledge.R.raw.gradients)
            .bufferedReader()
            .use { it.readText() }
        val json = JSONArray(string)

        return Gson().fromJson<List<ObjectGradient>>(
            json.toString(),
            object : TypeToken<List<ObjectGradient>>() {}.type
        )!!
    }

    fun createBitmapFromView(context: Context,post: Post,bitmap: Bitmap): Bitmap? {
        val view = LayoutInflater.from(context)
            .inflate(com.applifehack.knowledge.R.layout.item_share_post, null)
        view.findViewById<TextView>(R.id.title).text = post.title
        view.findViewById<ImageView>(R.id.image).setImageBitmap(bitmap)
        val point = displayDimensions(context)
        view.measure(
            View.MeasureSpec.makeMeasureSpec(point.x-50, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(point.y-300, View.MeasureSpec.EXACTLY)
        )
        val bitmap =
            Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.draw(canvas)
//        val createBitmap =
//            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
//        view.draw(Canvas(createBitmap))
        return bitmap
    }

    fun generateBitmap(context: Context, quoteObject: QuoteResp): Bitmap {
        val layout = LayoutInflater.from(context)
            .inflate(com.applifehack.knowledge.R.layout.inflator_quote_empty, null)
        val card = layout.findViewById<RelativeLayout>(R.id.card)
        val quote = layout.findViewById<TextView>(R.id.quote);
        val authorText = layout.findViewById<TextView>(R.id.author)
        val image = layout.findViewById<ImageView>(R.id.image)
        val gradient = layout.findViewById<FrameLayout>(R.id.gradient)
        val authorLayout = layout.findViewById<FrameLayout>(R.id.authorLayout)
        val logo = layout.findViewById<ImageView>(R.id.logo)

        // set values
        image.setImageBitmap(quoteObject.image)
        // authorText.text = quoteObject.author
        quote.text = quoteObject.quote

        // set dimensions for card
        val point = displayDimensions(context)
        val margin = dpToPx(16, context)
        val x = point.x
        val y = x

        val params = FrameLayout.LayoutParams(x, y)
        card.layoutParams = params


        // new params
        val paramsNQ = RelativeLayout.LayoutParams(x, 3 * y / 4)
//        val paramsNA = RelativeLayout.LayoutParams(
//            authorLayout.layoutParams.width,
//            authorLayout.layoutParams.height
//        )
        val paramsL = RelativeLayout.LayoutParams(
            logo.layoutParams.width,
            logo.layoutParams.height
        )

        // alignment quote
        quote.gravity = when (quoteObject.quoteAlign) {
            0 -> Gravity.LEFT
            1 -> Gravity.CENTER
            else -> Gravity.RIGHT
        }

        // align author
//        when (quoteObject.authorAlign) {
//            0 -> paramsNA.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
//            1 -> paramsNA.addRule(RelativeLayout.CENTER_HORIZONTAL)
//            2 -> paramsNA.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//        }

        // align logo
//        when (quoteObject.authorAlign) {
//            0, 1 -> paramsL.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//            2 -> paramsL.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
//        }

        // set new params
        paramsNQ.setMargins(margin)
        paramsL.setMargins(margin, 0, margin, margin)
        //paramsNA.setMargins(margin, 0, margin, margin)
        // paramsNA.addRule(RelativeLayout.BELOW, com.applifehack.knowledge.R.id.quote)
        paramsL.addRule(RelativeLayout.BELOW, com.applifehack.knowledge.R.id.quote)

        quote.layoutParams = paramsNQ
        // authorLayout.layoutParams = paramsNA
        logo.layoutParams = paramsL

        // set gradients
        gradient.setGradient(quoteObject.gradient!!, 0, quoteObject.angle!!)
        // authorLayout.setGradient(quoteObject.authorGradient!!, 16)

        // prepare for export
        layout.measure(
            View.MeasureSpec.makeMeasureSpec(x, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(y, View.MeasureSpec.EXACTLY)
        )
        layout.layout(0, 0, layout.measuredWidth, layout.measuredHeight)

        return getBitmapFromView(layout)
    }

}