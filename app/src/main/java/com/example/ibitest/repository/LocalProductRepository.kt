package com.example.ibitest.repository

import com.example.ibitest.room.ProductDao
import com.example.ibitest.model.Product
import javax.inject.Inject

class LocalProductRepository @Inject constructor(private val productDao: ProductDao) {

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
}