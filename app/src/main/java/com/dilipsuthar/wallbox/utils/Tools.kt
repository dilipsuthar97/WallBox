package com.dilipsuthar.wallbox.utils

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.TypedValue
import android.view.Menu
import androidx.core.content.ContextCompat
import com.dilipsuthar.wallbox.R
import java.nio.file.Files.size





object Tools {

    const val GONE = 0
    const val INVISIBLE = 1

    fun setSystemBarColor(act: Activity, @ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = act.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = color
        }
    }

    fun changeNavigationIconColor(toolbar: Toolbar, @ColorInt color: Int) {
        val drawable = toolbar.navigationIcon
        drawable?.mutate()
        drawable?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }

    fun changeMenuIconColor(menu: Menu, @ColorInt color: Int) {
        for (i in 0 until menu.size()) {
            val drawable = menu.getItem(i).icon ?: continue
            drawable.mutate()
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    fun setSystemBarLight(act: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            act.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    fun clearSystemBarLight(act: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            act.window.decorView.systemUiVisibility = 0
        }
    }

    fun enableViews(vararg views: View) {
        for (v in views) {
            v.isEnabled = true
        }
    }

    fun disableViews(vararg views: View) {
        for (v in views) {
            v.isEnabled = false
        }
    }

    fun visibleViews(vararg views: View) {
        for (v in views) {
            v.visibility = View.VISIBLE
        }
    }

    fun inVisibleViews(vararg views: View, type: Int) {

        if (type == INVISIBLE) {
            for (v in views) {
                v.visibility = View.INVISIBLE
            }
        } else {
            for (v in views) {
                v.visibility = View.GONE
            }
        }
    }

    fun setSnackBarBgColor(ctx: Context, snackbar: Snackbar, color: Int) {
        val sbView = snackbar.view
        sbView.setBackgroundColor(ctx.resources.getColor(color))
    }

    fun hasNetwork(ctx: Context?): Boolean? {
        var isConnected: Boolean? = false
        val connectivityManager = ctx?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }

}