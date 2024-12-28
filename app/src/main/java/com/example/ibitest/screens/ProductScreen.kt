package com.example.ibitest.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibitest.R
import com.example.ibitest.components.VerticallyScrollableText
import com.example.ibitest.model.Product
import com.example.ibitest.utils.mockDealsList

@Composable
fun ProductScreen(
    modifier: Modifier = Modifier,
    product: Product,
    isFavorite: Boolean,
    onFavoriteProductClicked: () -> Unit = {  }
) {
    val pagerState = rememberPagerState(pageCount = {
        product.images.size
    })
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
        Box(
            modifier = Modifier
                .heightIn(max = 320.dp)
                .background(color = Color.Transparent)
                .clipToBounds()
        ) {
            HorizontalPager(state = pagerState) { page ->
                AsyncImage(
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    model = product.images[page],
                    contentDescription = null,
                )
            }

            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 2.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) Color.Black else Color.Transparent
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .padding(4.dp)
                            .clip(CircleShape)
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = CircleShape
                            )
                            .background(color)
                    )
                }
            }
        }

        Text(
            modifier = Modifier.padding(20.dp),
            text = product.title,
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
                    append(product.brand)
                })

                Text(modifier = Modifier.padding(top = 6.dp), text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(R.string.price_label))
                    }
                    append(product.price)
                })
            }

            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Outlined.Favorite.takeIf { isFavorite } ?: Icons.Outlined.FavoriteBorder,
                tint = Color.Red,
                contentDescription = "Image Favorite",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        Toast
                            .makeText(context,
                                context
                                    .getString(R.string.product_added_to_favorites)
                                    .takeIf { !isFavorite }
                                    ?: context.getString(R.string.product_removed_from_favorites),
                                Toast.LENGTH_SHORT)
                            .show()
                        onFavoriteProductClicked()
                    }
            )
        }
        VerticallyScrollableText(
            product.description, modifier = Modifier
                .height(200.dp)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ProductScreenPreview() {
    ProductScreen(
        product = mockDealsList[0],
        isFavorite = true
    )
}