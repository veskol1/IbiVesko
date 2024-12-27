package com.example.ibitest.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.ibitest.R
import com.example.ibitest.model.Product
import com.example.ibitest.utils.mockDealsList


@Composable
fun ProductItem(
    product: Product,
    navigateOnProductClick: (String) -> Unit,
    isLastItem: Boolean = false
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(vertical = 10.dp)
            .clickable {
                navigateOnProductClick(product.id)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row {
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(0.dp)
                    .fillMaxHeight()
                    .background(color = Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = product.image,
                    contentDescription = null,
                )
            }
            Column(
                modifier = Modifier
                    .weight(5f)
                    .padding(8.dp)
                    .fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = product.title,
                    maxLines = 2,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(R.string.brand_label))
                        }
                        append(product.brand)
                    },
                    maxLines = 2,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(R.string.price_label))
                        }
                        append(product.price)
                    },
                    maxLines = 2,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 10.sp
                )
            }
        }
    }
    if (isLastItem) {
        Spacer(modifier = Modifier.height(100.dp))
    }

}

@Composable
@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
fun ProductItemPreview() {
    ProductItem(
        product = mockDealsList[0],
        navigateOnProductClick = {},
    )
}

