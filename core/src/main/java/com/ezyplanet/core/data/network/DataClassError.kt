package com.ezyplanet.core.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataClassError{
     data class  ErrorResp internal constructor(@Expose
                                                @SerializedName("attribute") val attribute:String,
                                                @Expose
                                                @SerializedName("full_messages")
                                                val full_messages:List<String>)
    data class ErrorResp2 internal  constructor(@Expose
                                               @SerializedName("error")
                                               val error: String,
                                               @Expose
                                               @SerializedName("error_description")
                                               val error_description:String)

    data class BaseError internal  constructor(@Expose
                                               @SerializedName("attribute")
                                               val attribute: String,
                                               @Expose
                                               @SerializedName("message")
                                               val message:String)

  data class ErrorsRespt internal constructor(@Expose
                                              @SerializedName("errors")
                                              val errors:List<BaseError>)
 }