package com.dilipsuthar.wallbox.utils

import android.widget.ImageView
import androidx.core.graphics.ColorUtils
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*

fun ImageView.loadUrl(url: String) {
    Picasso.get()
        .load(url)
        .into(this)
}

fun ImageView.loadUrl(url: String, placeHolder: Int = 0 , errorHolder: Int = 0) {
    Picasso.get()
        .load(url)
        .placeholder(placeHolder)
        .error(errorHolder)
        .into(this)
}

infix fun SwipeRefreshLayout.setRefresh(refreshing: Boolean) {
    if (refreshing) this.isRefreshing = refreshing
    else if (this.isRefreshing) this.isRefreshing = refreshing
}

fun Int.isDark(): Boolean = ColorUtils.calculateLuminance(this) < 0.5

infix fun Boolean.eq(value: Boolean): Boolean {
    return this == value
}

fun Int.getFormattedNumber(): String {
    return NumberFormat.getNumberInstance(Locale.US).format(this)
}
