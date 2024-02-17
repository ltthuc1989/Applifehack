package com.ltthuc.preferences.api

import kotlinx.coroutines.flow.Flow

interface DatastoreRepository {
    suspend fun getTheme(): Flow<Int?>
    suspend fun isPurchased(): Boolean
    suspend fun setPurchased(boolean: Boolean)
    suspend fun inCreaseCountAppLaunching()
    suspend fun getCountAppLaunching(): Int
    suspend fun resetCountAppLaunching()


}