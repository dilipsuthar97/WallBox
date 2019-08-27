package com.dilipsuthar.wallbox.helpers

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.annotation.NonNull
import android.os.Build
import android.os.LocaleList
import androidx.core.content.ContextCompat
import com.dilipsuthar.wallbox.preferences.Preferences
import java.util.*
import androidx.core.os.ConfigurationCompat.getLocales
import com.dilipsuthar.wallbox.R
import kotlin.collections.ArrayList


object LocaleHelper {
    fun loadLocal(context: Context?) {
        val sharedPreferences = Preferences.getSharedPreferences(context)
        val locale = Locale(sharedPreferences?.getString(Preferences.LANGUAGE_CODE, "en")!!)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.setLocale(locale)
        context?.let {
            it.resources.updateConfiguration(configuration, it.resources.displayMetrics)
        }

    }

    fun getLocale(context: Context?): Locale {
        val sharedPreferences = Preferences.getSharedPreferences(context)
        return Locale(sharedPreferences?.getString(Preferences.LANGUAGE_CODE, "en")!!)
    }
}