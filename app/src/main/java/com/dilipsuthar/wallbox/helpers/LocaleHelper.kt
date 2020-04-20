package com.dilipsuthar.wallbox.helpers

import android.content.Context
import android.content.res.Configuration
import com.dilipsuthar.wallbox.preferences.Prefs
import com.dilipsuthar.wallbox.preferences.SharedPref
import java.util.*

/**
 * Created by DILIP SUTHAR
 */
object LocaleHelper {
    fun loadLocal(context: Context?) {
        val locale = Locale(SharedPref.getInstance(context!!).getString(Prefs.LANGUAGE_CODE, "en")!!)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.setLocale(locale)
        context?.let {
            it.resources.updateConfiguration(configuration, it.resources.displayMetrics)
        }

    }

    fun getLocale(context: Context?): Locale {
        return Locale(SharedPref.getInstance(context!!).getString(Prefs.LANGUAGE_CODE, "en")!!)
    }
}