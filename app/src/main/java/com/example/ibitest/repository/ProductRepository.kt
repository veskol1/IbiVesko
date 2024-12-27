package com.example.ibitest.repository

import com.example.ibitest.api.ApiService
import com.example.ibitest.model.Product

import javax.inject.Inject

class ProductRepository @Inject constructor(private val api: ApiService) {
    private var productsList: MutableList<Product> = arrayListOf()

    suspend fun fetchProducts(limit: Int = 20, skip: Int = 0): List<Product> {

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

}