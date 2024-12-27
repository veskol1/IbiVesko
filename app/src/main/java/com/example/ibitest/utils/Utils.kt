package com.example.ibitest.utils

import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.Locale

object Utils {
    fun updateDeviceLocale(context: Context, selectedLanguage: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(selectedLanguage)
        } else {
            val newConfig = Configuration(context.resources.configuration)
            val locale = Locale(selectedLanguage)
            newConfig.setLocale(locale)
            context.resources.updateConfiguration(newConfig, context.resources.displayMetrics)
        }
    }
}