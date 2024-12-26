package com.example.ibitest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibitest.model.Product
import com.example.ibitest.repository.LocalProductRepository
import com.example.ibitest.repository.ProductRepository
import com.example.ibitest.constans.Constants.TIME_TO_CLEAR_IMAGE_CACHE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val remoteProductsRepository: ProductRepository,
    private val localRepository: LocalProductRepository,
    //private val connectivityManager: NetworkConnectivityManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState())
    var uiState = _uiState.asStateFlow()

    private val _productUiState = MutableStateFlow(ProductUiState())
    var productUiState = _productUiState.asStateFlow()

    private val _clearCache = MutableStateFlow(false)
    var clearCache = _clearCache.asStateFlow()

    init {
        getRemoteProducts()
        checkIfNeedToClearImageCache()
        //handleInternetConnectionState()
    }

    private fun getRemoteProducts(resetData: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            val productsList = remoteProductsRepository.fetchProducts(resetData = resetData)

            if (productsList.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        status = Status.SUCCESS,
                        productsList = productsList
                    )
                }
            } else {
                _uiState.value = ProductsUiState(status = Status.ERROR)
            }
        }
    }

    fun loadMoreProducts() {
        _uiState.update {
            it.copy(
                skip = it.skip + 10,
                isLoadingMore = true
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            val newProductsList = remoteProductsRepository.fetchProducts(
                skip = uiState.value.skip,
            )

            _uiState.update {
                it.copy(
                    status = Status.SUCCESS,
                    productsList = newProductsList,
                    isLoadingMore = false
                )
            }
        }
    }

    fun initProductScreenUi(productId: String) {
        val foundProduct = uiState.value.productsList.find { it.id == productId }!!
        viewModelScope.launch(Dispatchers.IO) {
            _productUiState.update {
                it.copy(
                    product = foundProduct,
                    isFavorite = localRepository.checkIfProductIsFavorite(foundProduct)
                )
            }
        }
    }

    fun handleFavoriteClicked() {
        val product = productUiState.value.product
        viewModelScope.launch(Dispatchers.IO) {
            if (productUiState.value.isFavorite) {
                localRepository.deleteProductFromDb(product)
            } else {
                localRepository.insertProductToDb(product)
            }

            _productUiState.update {
                it.copy(
                    isFavorite = !productUiState.value.isFavorite
                )
            }
        }
    }

    private fun checkIfNeedToClearImageCache() {
        viewModelScope.launch {
            remoteProductsRepository.savedCacheTime.take(1).collect { savedTime ->
                val currentTime = System.currentTimeMillis()
                val difference = currentTime - savedTime

                if (difference > 0) { //cache time is reached
                    _clearCache.value = true
                    remoteProductsRepository.saveCacheTimeToDataStore(currentTime + TIME_TO_CLEAR_IMAGE_CACHE)

                } else if (savedTime == 0L) { // init cache first time
                    remoteProductsRepository.saveCacheTimeToDataStore(currentTime + TIME_TO_CLEAR_IMAGE_CACHE)
                }
            }
        }
    }

//    private fun handleInternetConnectionState() {
//        connectivityManager.observe().onEach { status ->
//            when (status) {
//                ConnectivityObserver.Status.Available -> {
//                    if (uiState.value.procutsList.isEmpty()) {
//                        getRemoteMovies()
//                    } else {
//                        _uiState.update {
//                            it.copy(
//                                status = Status.SUCCESS
//                            )
//                        }
//                    }
//                }
//                ConnectivityObserver.Status.Lost -> {
//                    _uiState.update {
//                        it.copy(
//                            status = Status.NO_CONNECTION
//                        )
//                    }
//                }
//                else -> {}
//            }
//        }.launchIn(viewModelScope)
//    }
}

data class ProductsUiState(
    val status: Status = Status.LOADING,
    var skip: Int = 0,
    val productsList: List<Product> = emptyList(),
    val isLoadingMore: Boolean = false,
)

data class ProductUiState(
    val product: Product = Product(),
    val isFavorite: Boolean = false
)

enum class Status {
    LOADING,
    SUCCESS,
    ERROR,
    NO_CONNECTION
}
