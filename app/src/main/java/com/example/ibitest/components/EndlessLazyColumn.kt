package com.example.ibitest.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.ibitest.model.Product


@Composable
fun LazyColumnList(
    modifier: Modifier = Modifier,
    productsList: List<Product>,
    navigateOnProductClick: (String) -> Unit,
    loadMoreItems: () -> Unit = {},
    loadingProgressIndicator:  @Composable () -> Unit,
    isLoadingMore: Boolean = false,
    hideLastItemSpace: Boolean = false
) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = modifier,
        state = listState,
    ) {
        itemsIndexed(
            items = productsList,
            key = { _, item -> item.id }
        ) { index, product: Product ->
            ProductItem(
                product = product,
                navigateOnProductClick = { productId -> navigateOnProductClick(productId) },
                isLastItem = index == productsList.size - 1 && !hideLastItemSpace
            )
        }
    }


    if (isLoadingMore) {
        loadingProgressIndicator()
    }

    val reachedBottom: Boolean by remember { derivedStateOf { listState.reachedBottom() } }

    // load more if scrolled to bottom
    LaunchedEffect(reachedBottom) {
        if (reachedBottom && !isLoadingMore) {
            loadMoreItems()
        }
    }
}


internal fun LazyListState.reachedBottom(buffer: Int = 1): Boolean {
    val lastVisibleItem = this.layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index != 0 && lastVisibleItem?.index == this.layoutInfo.totalItemsCount - buffer
}