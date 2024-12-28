package com.example.ibitest.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ibitest.components.LazyColumnList
import com.example.ibitest.components.ProgressIndicator
import com.example.ibitest.utils.mockDealsList
import com.example.ibitest.viewmodel.ProductsUiState
import com.example.ibitest.viewmodel.Status

@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    productsUiState: ProductsUiState,
    navigateOnProductClick: (productId: String) -> Unit = {},
    loadMoreProducts: () -> Unit
) {

    when (productsUiState.status) {
        Status.SUCCESS -> {
            Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                Box {
                    LazyColumnList(
                        modifier = Modifier,
                        productsList = productsUiState.productsList,
                        navigateOnProductClick = { productId ->
                            navigateOnProductClick(productId)
                        },
                        loadMoreItems = { loadMoreProducts() },
                        loadingProgressIndicator = { ProgressIndicator(alignment = Alignment.BottomCenter) },
                        isLoadingMore = productsUiState.isLoadingMore
                    )
                }
            }
        }

        Status.LOADING -> {
            ProgressIndicator(alignment = Alignment.Center)
        }

        Status.ERROR -> {
            Box(modifier = modifier.fillMaxSize()) {
                Text(text = "Error loading data", modifier = Modifier.align(Alignment.Center) )
            }
        }

        Status.NO_CONNECTION -> {
            Box(modifier = modifier.fillMaxSize()) {
                Text(text = "No internet connection", modifier = Modifier.align(Alignment.Center) )
            }
        }
    }
}


@Composable
@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
fun ProductsScreenPreview() {
    ProductsScreen(
        productsUiState = ProductsUiState(
            status = Status.SUCCESS,
            productsList = mockDealsList
        ),
        navigateOnProductClick = {},
        loadMoreProducts = {}
    )
}
