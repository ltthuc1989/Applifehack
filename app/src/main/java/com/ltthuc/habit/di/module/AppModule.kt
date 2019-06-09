package com.ezyplanet.supercab.driver.di.module


import android.app.Application
import android.content.Context

import com.ezyplanet.core.di.qualifier.PreferenceInfo
import com.ezyplanet.core.util.CoreConstants
import com.ezyplanet.core.util.SchedulerProvider

import com.ezyplanet.thousandhands.shipper.data.preferences.AppPreferenceHelper
import com.irmansyah.catalogmoviekotlin.data.DataManager
import com.ltthuc.habit.data.AppDataManager
import com.ltthuc.habit.data.network.ApiHeader
import com.ltthuc.habit.data.network.ApiHelper
import com.ltthuc.habit.data.network.AppApiHelper

import com.ltthuc.habit.di.buider.ViewModelBuilder


import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import java.util.logging.LogManager
import javax.inject.Singleton

/**
 * Main App [Module] that provides default and public classes everywhere
 */
@Module(includes = [ViewModelBuilder::class])
class AppModule {

    /**
     * provides [Application] context as default context.
     */
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }




    @Provides
    @PreferenceInfo
    internal fun provideprefFileName(): String = CoreConstants.PREF_NAME

    @Provides
    internal fun provideApiKey(): String = "test"

    @Provides
    @Singleton
    internal fun provideApiHelper(appApiHelper: AppApiHelper): ApiHelper = appApiHelper


    @Provides
    @Singleton
    internal fun provideProtectedApiHeader(preferenceHelper: AppPreferenceHelper)
            : ApiHeader.ProtectedApiHeader = ApiHeader.ProtectedApiHeader(

            accessToken = "bearer "+preferenceHelper.token,language = preferenceHelper.userLanguage)

    @Provides
    @Singleton
    internal fun providePublicApiHeader(preferenceHelper: AppPreferenceHelper)
            : ApiHeader.PublicApiHeader = ApiHeader.PublicApiHeader(

            language = preferenceHelper.userLanguage)
    @Provides
    @Singleton
    internal fun provideDataManager(appDataManager: AppDataManager): DataManager {
        return appDataManager
    }






    @Provides
    internal fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    internal fun provideSchedulerProvider(): SchedulerProvider = SchedulerProvider()
}
