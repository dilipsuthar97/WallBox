package com.dilipsuthar.wallbox.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.preferences.PrefConst

object ThemeUtils {

    const val TAG: String = "ThemeUtils"
    const val LIGHT = "light"
    const val DARK = "dark"

    fun getTheme(context: Context): String {
        val sharedPreferences: SharedPreferences? = context.getSharedPreferences(PrefConst.PREF, Context.MODE_PRIVATE)
        return sharedPreferences?.getString(PrefConst.THEME, LIGHT)!!
    }

    fun setTheme(context: Context, theme: String) {
        val sharedPreferences: SharedPreferences? = context.getSharedPreferences(PrefConst.PREF, Context.MODE_PRIVATE)
        when (theme) {
            LIGHT -> sharedPreferences?.edit()?.putString(PrefConst.THEME, LIGHT)?.apply()
            else -> sharedPreferences?.edit()?.putString(PrefConst.THEME, DARK)?.apply()
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