package com.dilipsuthar.wallbox

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.util.Log
import com.dilipsuthar.wallbox.utils.ThemeUtils

class WallBox : Application() {

    companion object {

        const val TAG = "WallBox.debug"

        // Unsplash URL
        const val UNSPLASH_API_BASE_URL = "https://api.unsplash.com/"
        const val UNSPLASH_URL = "https://unsplash.com/"
        const val UNSPLASH_AUTH_URL = "https://unsplash.com/oauth/authorize"
        const val UNSPLASH_JOIN_URL = "https://unsplash.com/join"
        const val UNSPLASH_LOGIN_CALLBACK = "unsplash_auth"
        const val UNSPLASH_UTM_PARAMETERS = "?utm_source=wallbox&utm_medium=referral&utm_campaign=api-credit"

        const val DEFAULT_PER_PAGE = 30
        const val DEFAULT_SORT_PHOTOS = "latest"
        const val DEFAULT_SORT_COLLECTIONS = "all"

        const val DEFAULT_WALLPAPER_QUALITY = "Regular"

        const val DATE_FORMAT = "yyyy/MM/dd"
        const val DOWNLOAD_PATH = "/Pictures/WallBox/"
        const val DOWNLOAD_PHOTO_FORMAT = ".jpg"

        // Permission code
        const val REQUEST_CODE = 101

        private lateinit var instance: WallBox

        fun getInstance(): WallBox {
            return instance
        }

        fun getAccessKey(): String {
            Log.d(TAG, "Using release keys")
            return  BuildConfig.WALLBOX_ACCESS_KEY
        }

        fun getSecretKey(): String {
            Log.d(TAG, "Using release keys")
            return  BuildConfig.WALLBOX_SECRET_KEY
        }

        fun getLoginUrl(ctx: Context): String {
            return UNSPLASH_AUTH_URL +
                    "?client_id=" + getAccessKey() +
                    "&redirect_uri=" + "wallbox%3A%2F%2F" + UNSPLASH_LOGIN_CALLBACK +
                    "&response_type=" + "code" +
                    "&scope=" + "public+read_user+write_user+read_photos+write_photos+write_likes+read_collections+write_collections"
        }

        fun isDebug(ctx: Context): Boolean {
            try {
                return (ctx.applicationInfo.flags
                        and ApplicationInfo.FLAG_DEBUGGABLE) != 0
            } catch (ignored: Exception) {
            }

            return false
        }
    }

    // constructor
    init {
        instance = this
    }

    override fun onCreate() {
        when(ThemeUtils.getTheme(this)) {
            ThemeUtils.LIGHT -> setTheme(R.style.WallBox_Primary_Base_Light)
            ThemeUtils.DARK -> setTheme(R.style.WallBox_Primary_Base_Dark)
        }
        super.onCreate()
    }

    fun getContext(): Context = applicationContext

}