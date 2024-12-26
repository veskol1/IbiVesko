package com.example.ibitest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibitest.model.Product
import com.example.ibitest.repository.LocalProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val localRepository: LocalProductRepository
) : ViewModel() {

    private val _favoriteProductsUiState = MutableStateFlow<List<Product>>(emptyList())
    var favoriteProductsUiState = _favoriteProductsUiState.asStateFlow()

    private fun findSavedProduct(productId: String): Product {
        return favoriteProductsUiState.value.find { it.id == productId }!!
    }

    fun removeFavoriteProduct(productId: String) {
        val product = findSavedProduct(productId = productId)
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.deleteProductFromDb(product = product)
            updateFavoriteProductsState()
        }
    }

    fun updateFavoriteProductsState() {
        viewModelScope.launch(Dispatchers.IO) {
            _favoriteProductsUiState.update {
                localRepository.gelAllFavoriteProducts()
            }
        }
    }
}
