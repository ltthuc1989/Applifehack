package com.ezyplanet.core.ui.base.adapter


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.webkit.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import  com.ezyplanet.core.ui.widget.pager.*
import com.ezyplanet.core.GlideApp
import com.ezyplanet.core.ui.listener.OneClickListener
import com.ezyplanet.core.ui.widget.decoration.GridSpacingItemDecoration
import com.ezyplanet.core.ui.widget.decoration.SpaceItemDecoration
import com.ezyplanet.core.widgets.scoll.RecyclerViewScrollCallback
import com.google.firebase.storage.FirebaseStorage


class BindingAdapters {
    companion object {
        @JvmStatic
        @BindingAdapter(value = *arrayOf("visibleThreshold", "resetLoadingState", "onScrolledToBottom"), requireAll = false)
        fun setRecyclerViewScrollCallback(recyclerView: RecyclerView, visibleThreshold: Int, resetLoadingState: Boolean,
                                          onScrolledListener: RecyclerViewScrollCallback.OnScrolledListener) {

            val callback = RecyclerViewScrollCallback
                    .Builder(recyclerView.layoutManager)
                    .visibleThreshold(visibleThreshold)
                    .onScrolledListener(onScrolledListener)
                    .resetLoadingState(resetLoadingState)
                    .build()

            recyclerView.clearOnScrollListeners()
            recyclerView.addOnScrollListener(callback)
        }

        /**
         * @param recyclerView  RecyclerView to bind to SpaceItemDecoration
         * @param spaceInPx space in pixels
         */
        @JvmStatic
        @BindingAdapter("spaceItemDecoration")
        fun setSpaceItemDecoration(recyclerView: RecyclerView, spaceInPx: Float) {
            if (spaceInPx != 0f) {
                val itemDecoration = SpaceItemDecoration(spaceInPx.toInt(), true, false)
                recyclerView.addItemDecoration(itemDecoration)
            } else {
                val itemDecoration = SpaceItemDecoration(spaceInPx.toInt(), true, false)

                recyclerView.addItemDecoration(itemDecoration)
            }
        }

        /**
         * @param recyclerView  RecyclerView to bind to SpaceItemDecoration
         * @param spaceInPx space in pixels
         */
        @JvmStatic
        @BindingAdapter(value = ["spaceItemDecoration","showFirstDivider"] ,requireAll = false)
        fun setSpaceItemDecoration(recyclerView: RecyclerView, spaceInPx: Float,showFirstDivider:Boolean) {
            if (spaceInPx != 0f) {
                val itemDecoration = SpaceItemDecoration(spaceInPx.toInt(), showFirstDivider, false)
                recyclerView.addItemDecoration(itemDecoration)
            } else {
                val itemDecoration = SpaceItemDecoration(spaceInPx.toInt(), showFirstDivider, false)

                recyclerView.addItemDecoration(itemDecoration)
            }
        }


        /**
         * @param recyclerView  RecyclerView to bind to DividerItemDecoration
         * @param orientation 0 for LinearLayout.HORIZONTAL and 1 for LinearLayout.VERTICAL
         */
        @JvmStatic
        @BindingAdapter(value=["dividerItemDecoration","itemDecorationBackGround"],requireAll = false)
        fun setDividerItemDecoration(recyclerView: RecyclerView, orientation: Int,background:Drawable) {
            val itemDecoration = DividerItemDecoration(recyclerView.context, orientation)
            background?.let {
                itemDecoration.setDrawable(background)
            }

            recyclerView.addItemDecoration(itemDecoration)
        }

        @JvmStatic
        @BindingAdapter("pagerSnapDecoration")
        fun setPagerSnapDecoration(recyclerView: RecyclerView, itemPeekingPercent: Float) {
            if (itemPeekingPercent != 0f) {
                val cardWidthPixels = recyclerView.resources.displayMetrics.widthPixels * 0.70f
                val cardHintPercent = itemPeekingPercent
                val itemDecoration = RVPagerSnapFancyDecorator(recyclerView.context, cardWidthPixels.toInt(), cardHintPercent)
                recyclerView.addItemDecoration(itemDecoration)
            } else {
                val cardWidthPixels = recyclerView.resources.displayMetrics.widthPixels
                val itemDecoration = RVPagerSnapFancyDecorator(recyclerView.context, cardWidthPixels, 0f)
                recyclerView.addItemDecoration(itemDecoration)
            }
        }

        @JvmStatic
        @BindingAdapter("spaceGridItemDecoration")
        fun setSpaceGridItemDecoration(recyclerView: RecyclerView, spaceInPx: Float) {
            if (spaceInPx != 0f) {
                val itemDecoration = GridSpacingItemDecoration(spaceInPx.toInt(), true)
                recyclerView.addItemDecoration(itemDecoration)
            } else {
                val itemDecoration = SpaceItemDecoration(spaceInPx.toInt(), true, false)

                recyclerView.addItemDecoration(itemDecoration)
            }
        }


        @JvmStatic
        @BindingAdapter("requestFocusListener")
        fun requestFocus(editText: EditText, shouldFocus: Boolean) {
            if (shouldFocus) {
                editText.postDelayed({ editText.requestFocus() }, 100)
            } else {
                editText.postDelayed({ editText.clearFocus() }, 100)
            }
        }

        @JvmStatic
        @BindingAdapter("bindValue")
        fun bindEditTextValue(editText: EditText, field: MutableLiveData<String>) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    field.value = s.toString()
                }
            })
        }

        /*@JvmStatic
        @BindingAdapter("changeSearchViewFocusListener")
        fun changeSearchViewFocus(editText: EditText, searchIcon: AppCompatImageView) {
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    searchIcon.setImageResource(R.drawable.vector_search_black_87)
                } else {
                    searchIcon.setImageResource(R.drawable.vector_search_black_54)
                }
            }
        }*/

        @JvmStatic
        @BindingAdapter("srcImageUrl")
        fun setImageSrc(imageView: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                GlideApp.with(imageView.context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView)
            }
        }

        @JvmStatic
        @BindingAdapter("srcFireStorageUrl")
        fun setFireStorageImageSrc(imageView: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                val storage = FirebaseStorage.getInstance()
                val gsReference = storage.getReferenceFromUrl("url")
                GlideApp.with(imageView.context).load(gsReference).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView)
            }
        }

        @JvmStatic
        @BindingAdapter("srcImageAny")
        fun setImageSrcAny(imageView: ImageView, url: Any?) {
            when(url){
                is String->{
                    if (!url.isNullOrEmpty()) {
                        GlideApp.with(imageView.context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imageView)
                    }
                }
                is Int->{

                    GlideApp.with(imageView.context).load(url).into(imageView)

                }
            }

        }

        @JvmStatic
        @BindingAdapter("textViewIcon")
        fun setTextViewIcon(textView: TextView, url: String?) {
            if (!url.isNullOrEmpty()) {
                GlideApp.with(textView.context)
                        .load(url)
                        .into(object:Target<Drawable>{
                            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                textView.setCompoundDrawables(resource,null,null,null)
                            }

                            override fun onLoadStarted(placeholder: Drawable?) {
                            }

                            override fun onLoadFailed(errorDrawable: Drawable?) {
                            }

                            override fun getSize(cb: SizeReadyCallback) {
                            }

                            override fun getRequest(): Request? {
                                return null
                            }

                            override fun onStop() {
                            }

                            override fun setRequest(request: Request?) {
                            }

                            override fun removeCallback(cb: SizeReadyCallback) {
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }

                            override fun onStart() {
                            }

                            override fun onDestroy() {
                            }
                        })


            }
        }

        @JvmStatic
        @BindingAdapter("matchSrcImageUrl")
        fun setMatchImageSrc(imageView: ImageView, url: String?) {
            if (!url.isNullOrEmpty())
                GlideApp.with(imageView.context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(imageView)
        }

        @JvmStatic
        @BindingAdapter("srcImageBitmap")
        fun setImageBitmap(imageView: ImageView, bitmap: Bitmap?) {
            bitmap?.let {
                GlideApp.with(imageView.context).load(it)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView)
            }
        }

        @JvmStatic
        @BindingAdapter("srcImageDrawable")
        fun setImageDrawable(imageView: ImageView, drawable: Drawable?) {
            drawable?.let {
                GlideApp.with(imageView.context).load(it)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView)
            }
        }

        @JvmStatic
        @BindingAdapter("app:srcCompat")
        fun setSrcCompatResId(imageView: AppCompatImageView, @DrawableRes drawableResId: Int) {
            imageView.setImageResource(drawableResId)
        }

        @JvmStatic
        @BindingAdapter("app:srcCompat")
        fun setSrcCompatDrawable(imageView: AppCompatImageView, drawable: Drawable) {
            imageView.setImageDrawable(drawable)
        }

        @JvmStatic
        @BindingAdapter("android:background")
        fun setBackgroundResId(view: View, @DrawableRes drawableResId: Int) {
            view.setBackgroundResource(drawableResId)
        }
        @JvmStatic
        @BindingAdapter("backgroundColor")
        fun setBackgroundColor(view: View, @DrawableRes drawableResId: Int) {
            view.setBackgroundColor(drawableResId)
        }
        @JvmStatic
        @BindingAdapter("webViewClient")
        fun setWebViewClient(view: WebView, client: WebViewClient) {

            val settings =view.settings
            settings.loadWithOverviewMode = true
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.databaseEnabled = true
            settings.useWideViewPort = true
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            view.clearHistory()
            view.clearFormData()
            view.clearCache(true)
            settings.cacheMode = WebSettings.LOAD_NO_CACHE

            if (Build.VERSION.SDK_INT >= 21) {
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                settings.databasePath = "/data/data/" + view.context.packageName + "/databases/"
            }
            view.webViewClient = client
        }
        @JvmStatic
        @BindingAdapter(value = ["webViewChromeClient"],requireAll = false)
        fun setWebViewChromeClient(view: WebView, client: WebChromeClient) {

            val settings =view.settings
            settings.loadWithOverviewMode = true
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.databaseEnabled = true
            settings.useWideViewPort = true
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            settings.loadWithOverviewMode = true

//            view.clearHistory();
//            view.clearFormData();
//            view.clearCache(true)
            settings.cacheMode = WebSettings.LOAD_NO_CACHE



            if (Build.VERSION.SDK_INT >= 21) {
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                settings.databasePath = "/data/data/" + view.context.packageName + "/databases/"
            }



            view.webChromeClient = client
        }
        @JvmStatic
        @BindingAdapter("loadUrl")
        fun loadUrl(view: WebView, url: String?) {
            view.loadUrl(url)
        }

        @JvmStatic
        @BindingAdapter("invisibleUnless")
        fun invisibleUnless(view: View, visible: Boolean) {
            view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        }

        @JvmStatic
        @BindingAdapter("enableUnless")
        fun enableUnless(view: View, enable: Boolean) {
            view.isEnabled= enable
        }

        @JvmStatic
        @BindingAdapter("goneUnless")
        fun goneUnless(view: View, visible: Boolean) {
            view.visibility = if (visible) View.VISIBLE else View.GONE
        }
        //Textview
//        @JvmStatic
//        @BindingAdapter("htmlText")
//        fun setHtmlText(textView: TextView, resId :Int) {
//            textView.setText(Html.fromHtml(textView.context.getString(resId)))
//
//        }
        @JvmStatic
        @BindingAdapter("htmlText")
        fun setHtmlText(textView: TextView, content :String?) {
            content?.let {
                textView.text = Html.fromHtml(content)
            }


        }

        @JvmStatic
        @BindingAdapter("onPulledToRefresh")
        fun setOnSwipeRefreshListener(swipeRefreshLayout: SwipeRefreshLayout, onPulledToRefresh: Runnable) {
            swipeRefreshLayout.setOnRefreshListener { onPulledToRefresh.run() }
        }

        @JvmStatic @BindingAdapter("oneClick")
        fun setOnClickListener(theze: View, f: View.OnClickListener?) {
            when (f) {
                null -> theze.setOnClickListener(null)
                else -> theze.setOnClickListener(OneClickListener(f))
            }
        }

        @JvmStatic
        @BindingAdapter(value=["autoResizeTextMin","autoResizeTextMax"],requireAll = false)
        fun setAutoResizeTextMin(view :TextView, minSize:Int, maxSize:Int) {
            TextViewCompat.setAutoSizeTextTypeWithDefaults(view, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)
            if(maxSize==0){
                TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(view, minSize, 14,2, TypedValue.COMPLEX_UNIT_SP)
            }else{
                TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(view, minSize, maxSize,2, TypedValue.COMPLEX_UNIT_SP)
            }

        }


//        @JvmStatic
//        @BindingAdapter(value = *arrayOf("textContentClickable", "textContentClick","textBold"), requireAll = false)
//        fun onContentClick(textView: TextView, content:Array<String?>, clickListener: View.OnClickListener,isBold:Boolean=false) {
//            val changeString = content[1] ?: ""
//            val totalString = content[0]+ changeString
//            val spanText = SpannableString(totalString)
//            val mySpannable = TextSpanable(textView.context,totalString, clickListener, textView.context.getResources().getColor(R.color.white), isBold)
//            spanText.setSpan(mySpannable,
//                    content[0]!!.length,
//                    totalString.length, 0)
//
//            textView.text = spanText
//            textView.movementMethod = LinkMovementMethod.getInstance()
//        }

    }
}