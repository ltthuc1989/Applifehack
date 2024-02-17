package com.ltthuc.preferences.impl

import com.ltthuc.preferences.api.DatastoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DefaultDatastoreRepository @Inject constructor(
    private val dataStore: DataStore
): DatastoreRepository {

    override suspend fun getTheme(): Flow<Int?> {
        return dataStore.getTheme()
    }

    override suspend fun isPurchased(): Boolean {
        return dataStore.isPurchased()
    }

    override suspend fun setPurchased(boolean: Boolean) {
        dataStore.setPurchased(boolean)
    }

    override suspend fun inCreaseCountAppLaunching() {
        return dataStore.increaseCountAppLaunching()
    }

    override suspend fun getCountAppLaunching(): Int {
        return  dataStore.getCountAppLaunching()
    }

    override suspend fun resetCountAppLaunching() {
        dataStore.resetCountAppLaunching()
    }
}