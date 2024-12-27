package com.example.ibitest.utils

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.Locale

object Utils {

    fun updateDeviceLocale(context: Context, selectedLanguage: String) {
        val newConfig = Configuration(context.resources.configuration)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(selectedLanguage)
        } else {
            val locale = Locale(selectedLanguage)
            newConfig.setLocale(locale)
            newConfig.setLayoutDirection(locale)
            context.resources.updateConfiguration(newConfig, context.resources.displayMetrics)
        }
    }
}