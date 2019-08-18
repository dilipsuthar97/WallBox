package com.dilipsuthar.wallbox

import android.app.Application
import android.content.Context
import com.dilipsuthar.wallbox.utils.ThemeUtils

class WallBox : Application() {

    companion object {

        const val TAG = "WallBox.debug"

        // Unsplash URL
        const val UNSPLASH_API_BASE_URL = "https://api.unsplash.com/"
        const val UNSPLASH_URL = "https://unsplash.com/"

        const val DEFAULT_PER_PAGE = 30
        const val DEFAULT_SORT_PHOTOS = "latest"
        const val DEFAULT_SORT_COLLECTIONS = "all"

        const val DEFAULT_WALLPAPER_QUALITY = "Regular"

        const val DATE_FORMAT = "yyyy/MM/dd"
        const val DOWNLOAD_PATH = "/Pictures/WallBox/"
        const val DOWNLOAD_PHOTO_FORMAT = ".jpg"

        // Permission code
        const val WRITE_EXTERNAL_STORAGE = 100

        private lateinit var instance: WallBox

        fun getInstance(): WallBox {
            return instance
        }
    }

    override fun onCreate() {
        when(ThemeUtils.getTheme(this)) {
            ThemeUtils.LIGHT -> setTheme(R.style.WallBox_Primary_Base_Light)
            ThemeUtils.DARK -> setTheme(R.style.WallBox_Primary_Base_Dark)
        }
        super.onCreate()
    }

    init {
        instance = this
    }

    fun getContext(): Context? {
        return applicationContext
    }

}