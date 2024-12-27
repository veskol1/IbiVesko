package com.example.ibitest.viewmodel

import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ibitest.connectivity.ConnectivityObserver
import com.example.ibitest.connectivity.NetworkConnectivityManager
import com.example.ibitest.model.Product
import com.example.ibitest.repository.LocalProductRepository
import com.example.ibitest.repository.ProductRepository
import com.example.ibitest.constans.Constants.TIME_TO_CLEAR_IMAGE_CACHE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val remoteProductsRepository: ProductRepository,
    private val localRepository: LocalProductRepository,
    private val connectivityManager: NetworkConnectivityManager
) : ViewModel() {

    private val _productsState = MutableStateFlow(ProductsUiState())
    var productsState = _productsState.asStateFlow()

    private val _productState = MutableStateFlow(ProductUiState())
    var productState = _productState.asStateFlow()

    private val _clearCache = MutableStateFlow(false)
    var clearCache = _clearCache.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    var uiState = _uiState.asStateFlow()

    private val loggedInFlow = localRepository.getLoginStatusFromDataStore.shareIn(
        viewModelScope, SharingStarted.Lazily, replay = 1
    )
    private val themeFlow = localRepository.getThemeFromDataStore.shareIn(
        viewModelScope, SharingStarted.Lazily, replay = 1
    )
    private val languageFlow = localRepository.getLanguageFromDataStore.shareIn(
        viewModelScope, SharingStarted.Lazily, replay = 1
    )

    init {
        initUi()
        getRemoteProducts()
        checkIfNeedToClearImageCache()
        handleInternetConnectionState()
    }

    private fun initUi() {
        viewModelScope.launch {
            combine(loggedInFlow, themeFlow, languageFlow) { loggedIn, themeDark, language ->
                val layoutDirection = LayoutDirection.Ltr.takeIf { language == "en" } ?: LayoutDirection.Rtl
                UiState(
                    loggedIn = loggedIn,
                    themeDark = themeDark,
                    language = language,
                    layoutDirection = layoutDirection,
                    initFinished = true
                )
            }.collect { updatedUiState ->
                _uiState.value = updatedUiState
            }
        }
    }

    private fun getRemoteProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val productsList = remoteProductsRepository.fetchProducts()

            if (productsList.isNotEmpty()) {
                _productsState.update {
                    it.copy(
                        status = Status.SUCCESS,
                        productsList = productsList
                    )
                }
            } else {
                _productsState.value = ProductsUiState(status = Status.ERROR)
            }
        }
    }

    fun loadMoreProducts() {
        _productsState.update {
            it.copy(
                skip = it.skip + 10,
                isLoadingMore = true
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            val newProductsList = remoteProductsRepository.fetchProducts(
                skip = productsState.value.skip,
            )

            _productsState.update {
                it.copy(
                    status = Status.SUCCESS,
                    productsList = newProductsList,
                    isLoadingMore = false
                )
            }
        }
    }

    fun initProductScreenUi(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val foundProduct = if (productsState.value.productsList.isNotEmpty()) {
                productsState.value.productsList.find { it.id == productId }!!
            } else {
                val favoriteProducts = localRepository.gelAllFavoriteProducts()
                favoriteProducts.find { it.id == productId }!!
            }

            _productState.update {
                it.copy(
                    product = foundProduct,
                    isFavorite = localRepository.checkIfProductIsFavorite(foundProduct)
                )
            }
        }
    }

    fun handleFavoriteClicked() {
        val product = productState.value.product
        viewModelScope.launch(Dispatchers.IO) {
            if (productState.value.isFavorite) {
                localRepository.deleteProductFromDb(product)
            } else {
                localRepository.insertProductToDb(product)
            }

            _productState.update {
                it.copy(
                    isFavorite = !productState.value.isFavorite
                )
            }
        }
    }

    private fun checkIfNeedToClearImageCache() {
        viewModelScope.launch {
            localRepository.savedCacheTime.take(1).collect { savedTime ->
                val currentTime = System.currentTimeMillis()
                val difference = currentTime - savedTime

                if (difference > 0) { //cache time is reached
                    _clearCache.value = true
                    localRepository.saveCacheTimeToDataStore(currentTime + TIME_TO_CLEAR_IMAGE_CACHE)

                } else if (savedTime == 0L) { // init cache first time
                    localRepository.saveCacheTimeToDataStore(currentTime + TIME_TO_CLEAR_IMAGE_CACHE)
                }
            }
        }
    }


    fun switchThemeState(checked: Boolean) {
        viewModelScope.launch {
            localRepository.saveThemeToDataStore(themeDark = checked)
        }
        _uiState.update {
            it.copy(
                themeDark = checked
            )
        }
    }

    fun switchLanguageState(language: String) {
        viewModelScope.launch {
            localRepository.saveLanguageToDataStore(language = language)
        }
        val layoutDirection = LayoutDirection.Ltr.takeIf {  language == "en" } ?: LayoutDirection.Rtl
        _uiState.update {
            it.copy(
                language = language,
                layoutDirection = layoutDirection
            )
        }
    }

    fun updateLoggedInState(loggedIn: Boolean = false) {

        viewModelScope.launch {
            localRepository.saveLoginStatusToDataStore(status = loggedIn)
        }
        _uiState.update {
            it.copy(
                loggedIn = loggedIn,
            )
        }
    }

    private fun handleInternetConnectionState() {
        connectivityManager.observe().onEach { status ->
            when (status) {
                ConnectivityObserver.Status.Available -> {
                    if (productsState.value.productsList.isEmpty()) {
                        getRemoteProducts()
                    } else {
                        _productsState.update {
                            it.copy(
                                status = Status.SUCCESS
                            )
                        }
                    }
                }
                ConnectivityObserver.Status.Lost -> {
                    _productsState.update {
                        it.copy(
                            status = Status.NO_CONNECTION
                        )
                    }
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }
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

data class UiState(
    val themeDark: Boolean = false,
    val language: String = "en",
    val layoutDirection: LayoutDirection = LayoutDirection.Ltr,
    val loggedIn: Boolean = true,
    val initFinished: Boolean = false
)

enum class Status {
    LOADING,
    SUCCESS,
    ERROR,
    NO_CONNECTION
}
