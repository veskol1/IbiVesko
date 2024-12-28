package com.example.ibitest

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil3.imageLoader
import com.example.ibitest.components.BiometricPromptManager
import com.example.ibitest.navigation.BottomNavigation
import com.example.ibitest.navigation.NavigationGraph
import com.example.ibitest.ui.theme.IbiTestTheme
import com.example.ibitest.utils.Utils.updateDeviceLocale
import com.example.ibitest.viewmodel.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val promptManager by lazy {
        BiometricPromptManager(this)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val productsViewModel: ProductsViewModel = viewModel()
            val navController = rememberNavController()
            val uiState by productsViewModel.appState.collectAsStateWithLifecycle()

            if (uiState.initFinished) {
                updateDeviceLocale(context = LocalContext.current, selectedLanguage = uiState.language)
                CompositionLocalProvider(
                    LocalLayoutDirection provides uiState.layoutDirection
                ) {
                    IbiTestTheme (
                        darkTheme = uiState.themeDark,
                        content = {
                            Scaffold(modifier = Modifier.fillMaxSize(),
                                bottomBar = {
                                    if(uiState.loggedIn) {
                                        BottomNavigation(navController = navController)
                                    }
                                },
                                topBar = {
                                    TopAppBar(
                                        title = { Text(stringResource(R.string.app_name)) }
                                    )
                                }) { innerPadding ->
                                NavigationGraph(
                                    innerPadding = innerPadding,
                                    navController = navController,
                                    productsViewModel = productsViewModel,
                                    promptManager = promptManager
                                )
                            }

                            val clearImageCacheState by productsViewModel.clearCache.collectAsStateWithLifecycle()
                            if (clearImageCacheState) {
                                imageLoader.diskCache?.clear()
                            }
                        }
                    )
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