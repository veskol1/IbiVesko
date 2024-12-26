package com.example.ibitest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil3.imageLoader
import com.example.ibitest.navigation.BottomNavigation
import com.example.ibitest.navigation.NavigationGraph
import com.example.ibitest.ui.theme.IbiTestTheme
import com.example.ibitest.viewmodel.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IbiTestTheme {
                val navController = rememberNavController()
                val productsViewModel: ProductsViewModel = viewModel()

                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigation(navController = navController)
                    },
                    topBar = {
                        TopAppBar(
                            title = { Text("IBI products") }
                        )
                    }) { innerPadding ->
                    NavigationGraph(
                        innerPadding = innerPadding,
                        navController = navController,
                        productsViewModel = productsViewModel
                    )
                }

                val clearImageCacheState by productsViewModel.clearCache.collectAsStateWithLifecycle()
                if (clearImageCacheState) {
                    imageLoader.diskCache?.clear()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IbiTestTheme {

    }
}