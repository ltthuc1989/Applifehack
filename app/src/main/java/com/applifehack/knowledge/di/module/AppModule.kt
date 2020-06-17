package com.ezyplanet.supercab.driver.di.module


import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

import com.ezyplanet.core.di.qualifier.PreferenceInfo
import com.ezyplanet.core.util.CoreConstants
import com.ezyplanet.core.util.SchedulerProvider

import com.ezyplanet.thousandhands.shipper.data.preferences.AppPreferenceHelper
import com.google.firebase.analytics.FirebaseAnalytics
import com.irmansyah.catalogmoviekotlin.data.DataManager
import com.applifehack.knowledge.data.AppDataManager
import com.applifehack.knowledge.data.local.db.AppDatabase
import com.applifehack.knowledge.data.network.ApiHeader
import com.applifehack.knowledge.data.network.ApiHelper
import com.applifehack.knowledge.data.network.AppApiHelper

import com.applifehack.knowledge.di.buider.ViewModelBuilder
import com.ezyplanet.supercab.data.local.db.AppDbHelper
import com.ezyplanet.supercab.data.local.db.DbHelper


import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
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
    @Singleton
    internal fun provideAppDatabase(context: Context): AppDatabase =

        Room.databaseBuilder(context, AppDatabase::class.java, CoreConstants.APP_DB_NAME)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    Log.e("Database", "open")
                    super.onOpen(db)

                }

                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Log.e("Database", "create")
                }

            }).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    internal fun provideDbHelper(appDatabase: AppDatabase): DbHelper = AppDbHelper(appDatabase.postDao())

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

    @Provides
    @Singleton
    internal fun provideFirebaseAnylytics(context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }
}
