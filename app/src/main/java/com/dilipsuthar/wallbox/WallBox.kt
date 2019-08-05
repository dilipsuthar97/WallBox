package com.dilipsuthar.wallbox

import android.app.Application
import com.dilipsuthar.wallbox.utils.ThemeUtils

class WallBox : Application() {

    companion object {

        public const val TAG = "WallBox.debug"

        // Unsplash URL
        public const val UNSPLASH_API_BASE_URL = "https://api.unsplash.com/"
        public const val UNSPLASH_URL = "https://unsplash.com/"

        public const val DEFAULT_PER_PAGE = 30
        public const val DEFAULT_ORDER_BY = "latest"

        public const val DATE_FORMAT = "yyyy/MM/dd"
        public const val DOWNLOAD_PATH = "/Pictures/WallBox/"
        public const val DOWNLOAD_PHOTO_FORMAT = ".jpg"

        // Permission code
        public const val WRITE_EXTERNAL_STORAGE = 100

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
        initialize()
    }

    private fun initialize() {
        instance = this
    }

}