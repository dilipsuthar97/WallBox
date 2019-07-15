package com.dilipsuthar.wallbox

import android.app.Application
import com.dilipsuthar.wallbox.utils.ThemeUtils

class WallBox : Application() {

    companion object {
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