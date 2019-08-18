package com.dilipsuthar.wallbox.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.preferences.Preferences
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent



object ThemeUtils {

    const val TAG: String = "ThemeUtils"
    const val LIGHT = "light"
    const val DARK = "dark"

    fun getTheme(context: Context): String {
        val sharedPreferences = Preferences.getSharedPreferences(context)
        return sharedPreferences?.getString(Preferences.THEME, LIGHT)!!
    }

    fun setTheme(context: Context?, theme: String) {
        val sharedPreferences = Preferences.getSharedPreferences(context)
        when (theme) {
            LIGHT -> sharedPreferences?.edit()?.putString(Preferences.THEME, LIGHT)?.apply()
            DARK -> sharedPreferences?.edit()?.putString(Preferences.THEME, DARK)?.apply()
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

    fun restartApp(context: Context?) {
        val i = context!!.packageManager
            .getLaunchIntentForPackage(context.packageName)
        i?.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        i?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)
    }


}