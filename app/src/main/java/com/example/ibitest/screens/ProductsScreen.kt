package com.example.ibitest.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ibitest.components.LazyColumnList
import com.example.ibitest.components.ProgressIndicator
import com.example.ibitest.utils.mockDealsList
import com.example.ibitest.viewmodel.ProductsViewModel
import com.example.ibitest.viewmodel.Status

@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    productsViewModel: ProductsViewModel = viewModel(),
    bottomBarVisible: (Boolean) -> Unit,
    navigateOnProductClick: (productId: String) -> Unit = {}
) {
    val uiState by productsViewModel.productsState.collectAsStateWithLifecycle()

    when (uiState.status) {
        Status.SUCCESS -> {
            Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                Box {
                    LazyColumnList(
                        modifier = Modifier,
                        productsList = uiState.productsList,
                        navigateOnProductClick = { productId ->
                            navigateOnProductClick(productId)
                        },
                        bottomBarVisible = bottomBarVisible,
                        loadMoreItems = {
                            productsViewModel.loadMoreProducts()
                        },
                        loadingProgressIndicator = { ProgressIndicator(alignment = Alignment.BottomCenter) },
                        isLoadingMore = uiState.isLoadingMore
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
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            LazyColumnList(
                modifier = Modifier.padding(
                    top = 40.dp,
                    bottom = 200.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                productsList = mockDealsList,
                navigateOnProductClick = {},
                bottomBarVisible = { true },
                loadMoreItems = {},
                loadingProgressIndicator = {},
                isLoadingMore = false,
                hideLastItemSpace = false,
            )
        }
    }


}
