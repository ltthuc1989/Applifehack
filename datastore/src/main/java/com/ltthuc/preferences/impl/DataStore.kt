package com.ltthuc.preferences.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "air_quality_datastore")

    private val THEME = intPreferencesKey("theme")
    private val PURCHASED = booleanPreferencesKey("purchased")
    private val COUNT_APP_LAUNCHING = intPreferencesKey("count_app_launching")


    private suspend fun <T> save(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { settings ->
            settings[key] = value
        }
    }

    private suspend fun <T> get(key: Preferences.Key<T>): T? =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[key]
            }
            .first()

    private suspend fun <T> getFlow(key: Preferences.Key<T>): Flow<T?> {
      return  context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[key]
            }

    }

    suspend fun getTheme(): Flow<Int?> {
        return getFlow(THEME)
    }

    suspend fun isPurchased(): Boolean {
        return get(PURCHASED)?: false
    }

    suspend fun setPurchased(boolean: Boolean) {
        save(PURCHASED, boolean)
    }

    suspend fun increaseCountAppLaunching() {
        val count = getCountAppLaunching()
        if (count != -1 && count < 3) {
            save(COUNT_APP_LAUNCHING, count + 1)
        }
    }

    suspend fun getCountAppLaunching(): Int {
        return  get(COUNT_APP_LAUNCHING)?:0
    }

    suspend fun resetCountAppLaunching() {
        save(COUNT_APP_LAUNCHING, -1)
    }

}