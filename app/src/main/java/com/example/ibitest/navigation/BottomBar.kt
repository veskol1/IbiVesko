package com.example.ibitest.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBar(
    val route: String,
    val name: String,
    val icon : ImageVector,
    val selectedIcon : ImageVector
) {
    data object Products: BottomBar(
        route = "products",
        name = "Products",
        icon = Icons.Outlined.ShoppingCart,
        selectedIcon = Icons.Filled.ShoppingCart
    )

    data object Favorites: BottomBar(
        route = "favorites",
        name = "Favorites",
        icon = Icons.Outlined.FavoriteBorder,
        selectedIcon = Icons.Filled.Favorite
    )

    data object Settings: BottomBar(
        route = "settings",
        name = "Settings",
        icon = Icons.Outlined.Settings,
        selectedIcon = Icons.Filled.Settings
    )

}