package com.dilipsuthar.wallbox.activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.utils.ThemeUtils
import com.dilipsuthar.wallbox.utils.Tools

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        when(ThemeUtils.getTheme(this)) {
            ThemeUtils.LIGHT -> setTheme(R.style.WallBox_Primary_Base_Light)
            ThemeUtils.DARK -> setTheme(R.style.WallBox_Primary_Base_Dark)
        }
        super.onCreate(savedInstanceState)

        // Initialize emoji first
        val config: EmojiCompat.Config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)

        ThemeUtils.setRecentAppsHeaderColor(this)
        customizeStatusBar()

    }

    private fun customizeStatusBar() {
        when(ThemeUtils.getTheme(this)) {
            ThemeUtils.LIGHT -> Tools.setSystemBarLight(this)
            ThemeUtils.DARK -> Tools.clearSystemBarLight(this)
        }

        Tools.setSystemBarColor(this, ThemeUtils.getThemeAttrColor(this, R.attr.colorPrimaryDark))
    }
}