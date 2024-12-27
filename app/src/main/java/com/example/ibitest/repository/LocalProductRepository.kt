package com.example.ibitest.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.ibitest.room.ProductDao
import com.example.ibitest.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map

class LocalProductRepository @Inject constructor(private val productDao: ProductDao, private val dataStore: DataStore<Preferences>) {
    private val CACHE_TIME_KEY = longPreferencesKey("cache_image_time_key")
    private val THEME_DARK_KEY = booleanPreferencesKey("theme_dark_key")
    private val LANGUAGE_KEY = stringPreferencesKey("language_key")


    fun checkIfProductIsFavorite(product: Product): Boolean {
        return (productDao.getAll().find { it.id == product.id } != null)
    }

    fun insertProductToDb(product: Product) {
        productDao.insert(product = product)
    }

    fun deleteProductFromDb(product: Product) {
        productDao.delete(product = product)
    }

    fun gelAllFavoriteProducts(): List<Product> {
        return productDao.getAll()
    }

    suspend fun saveCacheTimeToDataStore(time: Long) {
        dataStore.edit { preferences ->
            preferences[CACHE_TIME_KEY] = time
        }
    }

    // Read data from DataStore
    val savedCacheTime: Flow<Long> = dataStore.data.map { preferences ->
        preferences[CACHE_TIME_KEY] ?: 0
    }

    suspend fun saveThemeToDataStore(themeDark: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_DARK_KEY] = themeDark
        }
    }

    // Read data from DataStore
    val getThemeFromDataStore: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[THEME_DARK_KEY] ?: false
    }

    suspend fun saveLanguageToDataStore(language: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }

    // Read data from DataStore
    val getLanguageFromDataStore: Flow<String> = dataStore.data.map { preferences ->
        preferences[LANGUAGE_KEY] ?: "en"
    }
}