package com.ezyplanet.thousandhands.util.device

interface BaseDeviceUtil {

    fun getAppVersion(): String?

    fun getAndroidVersion(): String

//    fun getSimSerialNumber(): String?

    fun getUniqueId(): String
}