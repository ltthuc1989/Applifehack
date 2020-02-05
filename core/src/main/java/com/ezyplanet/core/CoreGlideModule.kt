package com.ezyplanet.core

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.StorageReference
import java.io.InputStream
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import android.util.DisplayMetrics
import androidx.annotation.NonNull
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.annotation.GlideExtension



@GlideModule
class CoreGlideModule : AppGlideModule(){
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        // Register FirebaseImageLoader to handle StorageReference
        registry.append(StorageReference::class.java, InputStream::class.java,
                FirebaseImageLoader.Factory())
    }


}

