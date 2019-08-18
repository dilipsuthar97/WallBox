package com.dilipsuthar.wallbox.preferences

import android.content.Context
import android.content.SharedPreferences

class Preferences(context: Context) {

    companion object {

        // Preferences
        private const val PREF = "com.dilipsuthar.wallbox"
        const val FIRST_RUN = "first_run"
        const val SHOW_INTRO_TUTORIAL = "intro_tutorial"
        const val SORT = "sort"
        const val THEME = "theme"
        const val LANGUAGE = "language"
        const val LANGUAGE_CODE = "language_code"
        const val WALLPAPER_QUALITY = "wallpaper_preview_quality"

        // Constants
        const val PHOTO = "photo"
        const val COLLECTION = "collection"

        private var mPreferences: Preferences? = null

        fun getSharedPreferences(context: Context?): SharedPreferences? {
            return context?.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        }

        fun get(context: Context?): Preferences? {
            if (mPreferences == null ) {
                mPreferences = Preferences(context!!)
            }
            return mPreferences
        }
    }

}