package com.example.ibitest.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ProgressIndicator(
    size: Dp = 48.dp,
    alignment: Alignment = Alignment.BottomCenter
) {
    Box(modifier = Modifier.fillMaxSize().padding(30.dp)) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(size)
                .align(alignment),
            color = colorScheme.primary,
            trackColor = colorScheme.surfaceVariant,
        )
    }
}
