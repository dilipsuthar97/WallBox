package com.dilipsuthar.wallbox.helpers

import android.widget.ImageView
import androidx.core.graphics.ColorUtils
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.text.NumberFormat
import java.util.*

/**
 * load photo and apply into imageView
 *
 * @param url The url from where the photo will be load
 */
fun ImageView.loadUrl(url: String) {
    /*Picasso.get()
        .load(url)
        .into(this)*/
    Glide.with(this)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)   // Save image cache load from cache in future
        .into(this)
}

/**
 * load photo and apply into imageView and add placeHolder, errorHolder
 *
 * @param url The url from where the photo will be load
 * @param placeHolder The res id of placeHolder drawable
 * @param errorHolder The res id of errorHolder drawable
 */
fun ImageView.loadUrl(url: String, placeHolder: Int = 0 , errorHolder: Int = 0) {
    /*Picasso.get()
        .load(url)
        .placeholder(placeHolder)
        .error(errorHolder)
        .into(this)*/
    Glide.with(this)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)   // Save image cache load from cache in future
        .placeholder(placeHolder)
        .error(errorHolder)
        .into(this)
}

/**
 * set value of swipe refresh layout true or false
 *
 * @param refreshing The boolean value which make layout refresh start and stop
 */
infix fun SwipeRefreshLayout.setRefresh(refreshing: Boolean) {
    if (refreshing) this.isRefreshing = refreshing
    else if (this.isRefreshing) this.isRefreshing = refreshing
}

fun Int.isDark(): Boolean = ColorUtils.calculateLuminance(this) < 0.5

/**
 * compare the given boolean value with user's check predicate
 *
 * @param value The boolean value to check predicate
 */
infix fun Boolean.eq(value: Boolean): Boolean {
    return this == value
}

fun Int.getFormattedNumber(): String {
    return NumberFormat.getNumberInstance(Locale.US).format(this)
}
