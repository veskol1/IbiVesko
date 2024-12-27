package com.example.ibitest.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.ibitest.viewmodel.ProductsViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibitest.R
import com.example.ibitest.components.VerticallyScrollableText

@Composable
fun ProductScreen(
    modifier: Modifier = Modifier,
    productsViewModel: ProductsViewModel = viewModel(),
    onFavoriteProductClicked: () -> Unit = {  }
) {
    val productUiState by productsViewModel.productState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
        AsyncImage(
            modifier = Modifier.height(450.dp),
            model = productUiState.product.image,
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
        )

        Text(
            modifier = Modifier.padding(20.dp),
            text = productUiState.product.title,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            maxLines = 3
        )

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()) {
            Column {
                Text(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(R.string.brand_label))
                    }
                    append(productUiState.product.brand)
                })

                Text(modifier = Modifier.padding(top = 6.dp), text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(R.string.price_label))
                    }
                    append(productUiState.product.price)
                })
            }

            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Outlined.Favorite.takeIf { productUiState.isFavorite } ?: Icons.Outlined.FavoriteBorder,
                tint = Color.Red,
                contentDescription = "Image Favorite",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        onFavoriteProductClicked()
                    }
            )
        }
        VerticallyScrollableText(
            productUiState.product.description, modifier = Modifier
                .height(200.dp)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        )
    }
}
