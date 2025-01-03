package com.example.ibitest.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ibitest.R
import com.example.ibitest.components.AppThemeSwitch
import com.example.ibitest.components.LanguageSwitcher
import com.example.ibitest.viewmodel.ProductsViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    productsViewModel: ProductsViewModel = viewModel(),
    onLogout: () -> Unit = {}
) {
    val appState by productsViewModel.appState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        AppThemeSwitch(isDarkTheme = appState.themeDark, onThemeChange = {
            productsViewModel.switchThemeState(checked = it)
        })

        LanguageSwitcher(selectedLanguage = appState.language, onLanguageChange = {
            productsViewModel.switchLanguageState(language = it)
        })

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 30.dp)
                .fillMaxWidth(),
            onClick = {
                onLogout()
                productsViewModel.updateLoggedInState(loggedIn = false)
            }) {
            Text(text = stringResource(id = R.string.logout_label))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    SettingsScreen()
}



