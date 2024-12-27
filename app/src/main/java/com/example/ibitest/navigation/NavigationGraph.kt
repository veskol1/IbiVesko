package com.example.ibitest.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ibitest.components.BiometricPromptManager
import com.example.ibitest.viewmodel.FavoritesViewModel
import com.example.ibitest.viewmodel.ProductsViewModel
import com.example.ibitest.screens.FavoritesScreen
import com.example.ibitest.screens.LoginScreen
import com.example.ibitest.screens.ProductScreen
import com.example.ibitest.screens.ProductsScreen
import com.example.ibitest.screens.SettingsScreen

@Composable
fun NavigationGraph(
    innerPadding: PaddingValues,
    navController: NavHostController,
    productsViewModel: ProductsViewModel = viewModel(),
    favoritesViewModel: FavoritesViewModel = viewModel(),
    promptManager: BiometricPromptManager
) {
    val uiState by productsViewModel.uiState.collectAsState()
    val baseModifier = Modifier.padding(
        top = innerPadding.calculateTopPadding(),
        bottom = innerPadding.calculateBottomPadding(),
        start = 24.dp,
        end = 24.dp
    )
    NavHost(
        navController,
        startDestination = if (uiState.loggedIn) BottomBar.Products.route else "login",
    ) {
        composable(route = "login") {
            LoginScreen(
                modifier = Modifier,
                onSuccessfulLogin = {
                    productsViewModel.updateLoggedInState(loggedIn = true)
                    navController.navigate(BottomBar.Products.route) {
                        popUpTo("login") { inclusive = true }
                    }
                },
                promptManager = promptManager
            )
        }

        composable(route = BottomBar.Products.route) {
            ProductsScreen(
                modifier = baseModifier,
                productsViewModel = productsViewModel,
                navigateOnProductClick = { productId ->
                    productsViewModel.initProductScreenUi(productId)
                    navController.navigate("product")
                },
            )
        }

        composable(route = BottomBar.Favorites.route) {
            favoritesViewModel.updateFavoriteProductsState()
            FavoritesScreen(
                modifier = baseModifier,
                favoriteProductsViewModel = favoritesViewModel,
                navigateOnCardClick = { productId ->
                    productsViewModel.initProductScreenUi(productId)
                    navController.navigate("product")
                },
                removeFavoriteClick = { productId ->
                    favoritesViewModel.removeFavoriteProduct(productId)
                }
            )
        }

        composable(route = "product") {
            ProductScreen(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                    start = 24.dp,
                    end = 24.dp
                ),
                productsViewModel = productsViewModel,
                onFavoriteProductClicked = { productsViewModel.handleFavoriteClicked() })
        }


        composable(route = BottomBar.Settings.route) {
            SettingsScreen(
                modifier = baseModifier,
                productsViewModel = productsViewModel,
                onLogout = {
                    navController.navigate("login")
                }
            )
        }
    }
}