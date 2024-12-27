package com.example.ibitest.components

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ibitest.R
import com.example.ibitest.utils.Utils.updateDeviceLocale
import java.util.Locale

@Composable
fun LanguageSwitcher(selectedLanguage: String, onLanguageChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { expanded = !expanded }) {
            Text(text = stringResource(id = R.string.language_label))
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
                updateDeviceLocale(context, "en")
                onLanguageChange("en")
            }, text = { Text(stringResource(id = R.string.language_english)) })
            DropdownMenuItem(onClick = {
                expanded = false
                onLanguageChange("iw")
                updateDeviceLocale(context, "iw")
            }, text = { Text(stringResource(id = R.string.language_hebrew)) })
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(stringResource(id = R.string.language_hebrew).takeIf { selectedLanguage == "iw" } ?: stringResource(id = R.string.language_english))
    }
}


