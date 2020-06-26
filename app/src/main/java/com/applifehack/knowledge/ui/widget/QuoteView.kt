package com.applifehack.knowledge.ui.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setMargins
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ezyplanet.core.GlideApp
import com.applifehack.knowledge.R
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.databinding.WidgetQuoteBinding
import com.applifehack.knowledge.ui.widget.listener.QuoteViewListener
import com.applifehack.knowledge.util.setGradient
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.widget_quote.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.sourcei.kowts.utils.functions.F
import org.sourcei.kowts.utils.pojo.QuoteResp
import org.sourcei.kowts.utils.reusables.Angles

class QuoteView : FrameLayout{
    lateinit var binding: WidgetQuoteBinding

    lateinit var quoteObject: QuoteResp
    var _model:Post?=null
    var model:Post?
        get() = _model
        set(value) {
            _model= value
            binding.model=_model
        }

    lateinit var gradient: IntArray
    var bitmap: Bitmap? = null

    constructor(context: Context?) : super(context) {
        initInflate()

    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initInflate()

    }


    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initInflate()
    }

    fun initInflate() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = WidgetQuoteBinding.inflate(inflater, this, true)
        binding.model = _model
        binding.authorCard.setOnClickListener {
            binding.listener?.openAuthorWiki(_model?.authorUrl)

        }
        setDimensions(F.displayDimensions(context))
        getBitmap(bitmap,context)


    }

    fun getBitmap(bitmap: Bitmap?, context: Context) {
        binding.progress.visibility = View.VISIBLE
        binding.card.visibility = View.GONE
        GlobalScope.launch {
            val future = GlideApp.with(context)
                .asBitmap()
                .load("https://source.unsplash.com/random")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .submit()

            try {
                val bitmapN = future.get()
                F.compareBitmaps(bitmap, bitmapN) {
                    if (it)
                        getBitmap(bitmap, context)
                    else
                        (context as AppCompatActivity).runOnUiThread {
                            setQuote(bitmapN,context)


                        }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                (context as AppCompatActivity).runOnUiThread {

                }
            }
        }
    }

    private fun setQuote(bitmap: Bitmap?,context: Context?){

        val colors = F.randomGradient().toIntArray()
        // val colorsAuthor = F.randomGradient().toIntArray()
        val angle = Angles.random().toFloat()

        // create quote object
        quoteObject = QuoteResp(
            _model?.description,
            colors,
            angle,
            bitmap,
            getAlignment((0..2).random())

        )


        binding.background.setImageBitmap(bitmap)
        this.bitmap = bitmap
        setBackground(context,bitmap)
//        if(_model?.author.isNullOrEmpty()){
//            binding.authorCard.visibility = View.GONE
//        }else{
//            binding.authorCard.visibility = View.VISIBLE
//        }


        binding.gradient.setGradient(colors, 0, angle)
        blurMask.setGradient(colors, 0, angle)
        // authorLayout.setGradient(colorsAuthor, 16)

        // changeAuthorAlignment((0..2).random())
        binding.progress.visibility = View.GONE
        binding.card.visibility = View.VISIBLE




    }
    private fun getAlignment(align: Int) :Int{
        quote.gravity = when (align) {
            0 -> {
                return Gravity.LEFT
            }
            1 -> {
                return Gravity.CENTER
            }
            else -> {
                return Gravity.RIGHT
            }
        }
        return Gravity.LEFT
    }

    private fun changeAuthorAlignment(align: Int) {
        val params = authorCard.layoutParams as RelativeLayout.LayoutParams

        params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
        params.removeRule(RelativeLayout.CENTER_HORIZONTAL)
        params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)

        when (align) {
            0 -> {
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            }
            1 -> {
                params.addRule(RelativeLayout.CENTER_HORIZONTAL)
            }
            2 -> {
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            }
        }

        authorCard.layoutParams = params
        // quoteObject.authorAlign = align
    }


    private fun setBackground(context: Context?,bitmap: Bitmap?) {
        Blurry.with(context)
            .async()
            .sampling(1)
            .from(bitmap)
            .into(blurBg)
    }

    private fun setDimensions(point: Point) {

        // set dimensions for card
        val x = point.x - F.dpToPx(32, context)
        val y = x

        val params = RelativeLayout.LayoutParams(x, y)
        params.addRule(RelativeLayout.CENTER_HORIZONTAL)
        params.addRule(RelativeLayout.CENTER_VERTICAL)
        binding.card.layoutParams = params




        // set dimensions for quote & author layout
        val margin = F.dpToPx(16, context)

        // original params
        val paramsT = quote.layoutParams // original params for quote
        //  val paramsA = authorCard.layoutParams // original params for author

        // new params
        val paramsNQ = RelativeLayout.LayoutParams(paramsT.width, 3 * y / 4)
        // val paramsNA = RelativeLayout.LayoutParams(paramsA.width, paramsA.height)

        paramsNQ.setMargins(margin)
        //  paramsNA.setMargins(margin, 0, margin, margin)
        //  paramsNA.addRule(RelativeLayout.BELOW, R.id.quote)

        // set params
        // binding.authorCard.layoutParams = paramsNA
        binding.quote.layoutParams = paramsNQ
    }

    fun setListener(listener:QuoteViewListener){
        binding.listener = listener

    }
    fun getQuote():QuoteResp{
        return quoteObject
    }


}