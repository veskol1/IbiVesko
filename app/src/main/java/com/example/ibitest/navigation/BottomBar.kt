package com.example.ibitest.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ibitest.R

sealed class BottomBar(
    val route: String,
    @StringRes val name: Int,
    val icon : ImageVector,
    val selectedIcon : ImageVector
) {
    data object Products: BottomBar(
        route = "products",
        name = R.string.products_label,
        icon = Icons.Outlined.ShoppingCart,
        selectedIcon = Icons.Filled.ShoppingCart
    )

    data object Favorites: BottomBar(
        route = "favorites",
        name = R.string.favorites_label,
        icon = Icons.Outlined.FavoriteBorder,
        selectedIcon = Icons.Filled.Favorite
    )

    data object Settings: BottomBar(
        route = "settings",
        name = R.string.settings_label,
        icon = Icons.Outlined.Settings,
        selectedIcon = Icons.Filled.Settings
    )

}