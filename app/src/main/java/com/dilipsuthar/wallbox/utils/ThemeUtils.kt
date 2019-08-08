package com.dilipsuthar.wallbox.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.preference.PreferenceManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StringDef
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.preferences.Preferences
import java.lang.annotation.RetentionPolicy

object ThemeUtils {

    const val TAG: String = "ThemeUtils"
    const val LIGHT = "light"
    const val DARK = "dark"

    fun getTheme(context: Context): String {
        val sharedPreferences: SharedPreferences? = context.getSharedPreferences(Preferences.PREF, Context.MODE_PRIVATE)
        return sharedPreferences?.getString(Preferences.THEME, LIGHT)!!
    }

    fun setTheme(context: Context, theme: String) {
        val sharedPreferences: SharedPreferences? = context.getSharedPreferences(Preferences.PREF, Context.MODE_PRIVATE)
        when (theme) {
            LIGHT -> sharedPreferences?.edit()?.putString(Preferences.THEME, LIGHT)?.apply()
            else -> sharedPreferences?.edit()?.putString(Preferences.THEME, DARK)?.apply()
        }
    }

    @ColorInt
    fun getThemeAttrColor(context: Context, @AttrRes colorAttr: Int): Int {
        val array = context.obtainStyledAttributes(null, intArrayOf(colorAttr))
        try {
            return array.getColor(0, 0)
        } finally {
            array.recycle()
        }
    }

    fun setRecentAppsHeaderColor(activity: Activity) {
        val icon = BitmapFactory.decodeResource(activity.resources, R.mipmap.ic_launcher)
        val taskDescription = ActivityManager.TaskDescription(
            activity.getString(R.string.app_name),
            icon, getThemeAttrColor(activity, R.attr.colorPrimaryDark)
        )
        activity.setTaskDescription(taskDescription)
        icon?.recycle()
    }

}