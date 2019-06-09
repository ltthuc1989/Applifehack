package com.ezyplanet.supercab.driver.di.module




import com.ezyplanet.thousandhands.util.connectivity.BaseConnectionManager
import com.ezyplanet.thousandhands.util.connectivity.ConnectionManager
import com.ezyplanet.thousandhands.util.device.BaseDeviceUtil
import com.ezyplanet.thousandhands.util.device.DeviceUtilImpl
import com.ezyplanet.thousandhands.util.providers.BaseResourceProvider
import com.ezyplanet.thousandhands.util.providers.ResourceProvider
import com.ezyplanet.thousandhands.util.providers.file.BaseFileProvider
import com.ezyplanet.thousandhands.util.providers.file.FileProvider

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface UtilModule {

    /**
     * provide main implementation of [BaseConnectionManager] to check connection status
     */
    @Binds
    @Singleton
    fun bindConnectionManager(connectionManager: ConnectionManager): BaseConnectionManager

    /**
     * provide main implementation of [BaseDeviceUtil] to access device shared data, unique identifiers, etc
     */
    @Binds
    @Singleton
    fun bindDeviceUtil(deviceUtilImpl: DeviceUtilImpl): BaseDeviceUtil

    /**
     * Provide main implementation of [BaseResourceProvider] to access app raw resources
     */
    @Binds
    fun bindResourceProvider(resourceProvider: ResourceProvider): BaseResourceProvider

    /**
     * Provide main implementation of [BaseFileProvider]
     */
    @Binds
    fun bindFileProvider(fileProvider: FileProvider): BaseFileProvider




}