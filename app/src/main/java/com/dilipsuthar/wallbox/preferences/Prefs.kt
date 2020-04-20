package com.dilipsuthar.wallbox.preferences

import android.content.Context
import android.content.SharedPreferences

object Prefs {
    // preferences
    private const val PREF = "com.dilipsuthar.wallbox"

    // first run
    const val FIRST_RUN = "first_run"
    const val ACT_HOME_INTRO = "act_home_intro"
    const val ACT_SEARCH_INTRO = "act_search_intro"
    const val ACT_SETTING_INTRO = "act_setting_intro"
    const val SHOW_INTRO_TUTORIAL = "intro_tutorial"

    const val SORT = "sort"
    const val SEARCH_QUERY = "search_query"
    const val THEME = "theme"
    const val LANGUAGE = "language"
    const val LANGUAGE_CODE = "language_code"
    const val WALLPAPER_QUALITY = "wallpaper_preview_quality"

    // constants for passing bundle data
    const val PHOTO = "photo"
    const val COLLECTION = "collection"
    const val USER = "user"

    // auth prefs
    const val AUTH_TOKEN = "auth_token"
    const val AUTH_ID = "auth_id"
    const val AUTH_USERNAME = "auth_username"
    const val AUTH_FIRST_NAME = "auth_first_name"
    const val AUTH_LAST_NAME = "auth_last_name"
    const val AUTH_EMAIL = "auth_email"
    const val AUTH_PROFILE_URL = "auth_profile_url"

}