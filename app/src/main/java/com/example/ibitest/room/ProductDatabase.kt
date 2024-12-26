package com.example.ibitest.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ibitest.model.Product


@Database(entities = [Product::class], version = 1)
abstract class ProductDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
}