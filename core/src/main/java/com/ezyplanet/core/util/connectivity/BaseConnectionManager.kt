package com.ezyplanet.thousandhands.util.connectivity

/**
 * An interface to define status of device connectivity
 */
interface BaseConnectionManager {

    /**
     * attempts to check if device is connected to internet or not
     */
    fun isNetworkConnected(): Boolean?

    /**
     * attempts to check if device is connected to initView VPN
     */
    fun isVPNConnected(): Boolean?

    /**
     * get IP address that assigned to device
     */
    fun getIPV4(): String?

    /**
     * attempts to check if device is connected to network through initView wifi
     */
    fun isWifi(): Boolean?

    /**
     * attempts to check if device is connected to network through Mobile network
     */
    fun isMobileData(): Boolean?
}