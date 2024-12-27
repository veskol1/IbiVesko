package com.example.ibitest.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.ibitest.room.ProductDatabase
import com.example.ibitest.api.ApiService
import com.example.ibitest.constans.Constants.API_BASE_URL
import com.example.ibitest.repository.LocalProductRepository
import com.example.ibitest.repository.ProductRepository
import com.example.ibitest.room.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun getApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext appContext: Context): ProductDatabase {
        return Room.databaseBuilder(appContext,
            ProductDatabase::class.java, "database name"
        ).build()
    }

    @Provides
    fun provideDao(productDatabase: ProductDatabase): ProductDao = productDatabase.productDao()

    @Provides
    @Singleton
    fun productRemoteRepositoryProvide(apiService: ApiService): ProductRepository {
        return ProductRepository(apiService)
    }

    @Provides
    @Singleton
    fun productLocalRepositoryProvide(productDao: ProductDao, dataStore: DataStore<Preferences>): LocalProductRepository {
        return LocalProductRepository(productDao, dataStore)
    }

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create (produceFile = { appContext.preferencesDataStoreFile(name = "settings_preferences") })
    }

//    @Provides
//    @Singleton
//    fun provideConnectivity(@ApplicationContext appContext: Context): NetworkConnectivityManager {
//        return NetworkConnectivityManager(appContext)
//    }


}