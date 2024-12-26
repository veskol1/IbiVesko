package com.example.ibitest.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.example.ibitest.api.ApiService
import com.example.ibitest.model.Product

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepository @Inject constructor(private val api: ApiService, private val dataStore: DataStore<Preferences>) {
    private var productsList: MutableList<Product> = arrayListOf()
    private val cacheTimeKey = longPreferencesKey("cache_image_time_key")

    suspend fun fetchProducts(limit: Int = 20, skip: Int = 0, resetData: Boolean = false): List<Product> {
        if (resetData) {
            productsList.clear()
        }
        try {
            val response = api.getProducts(limit = limit, skip = skip)

            if (response.isSuccessful && response.body() != null) {
                val fetchedProductsList = (response.body()?.results ?: arrayListOf()).toMutableList()
                productsList.addAll(fetchedProductsList)
                return productsList.distinctBy { it.id }
            }
        } catch (e: Exception) {
            return emptyList()
        }

        return emptyList()
    }

    suspend fun saveCacheTimeToDataStore(time: Long) {
        dataStore.edit { preferences ->
            preferences[cacheTimeKey] = time
        }
    }

    val savedCacheTime: Flow<Long> = dataStore.data.map { preferences ->
        preferences[cacheTimeKey] ?: 0
    }

}