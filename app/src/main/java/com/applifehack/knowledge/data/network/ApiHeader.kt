package com.applifehack.knowledge.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.inject.Inject

/**
 * Created by jyotidubey on 11/01/18.
 */
class ApiHeader @Inject constructor(internal val publicApiHeader: PublicApiHeader, internal val protectedApiHeader: ProtectedApiHeader) {

    class PublicApiHeader constructor(
            @Expose
            @SerializedName("Accept-Language") var language: String?="zh-hant")

    class ProtectedApiHeader constructor(@Expose
                                                 @SerializedName("Authorization") var accessToken: String?,
                                                 @SerializedName("Accept-Language") var language: String?="zh-hant", @Expose
                                                 @SerializedName("X-Device-Type") var device_type: String? = "android")

}