package com.dilipsuthar.wallbox.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.utils.ThemeUtils

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        when(ThemeUtils.getTheme(this)) {
            ThemeUtils.LIGHT -> setTheme(R.style.WallBox_Primary_Base_Light)
            ThemeUtils.DARK -> setTheme(R.style.WallBox_Primary_Base_Dark)
        }

        super.onCreate(savedInstanceState)

        ThemeUtils.setRecentAppsHeaderColor(this)

    }

}