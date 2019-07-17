package com.dilipsuthar.wallbox.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.utils.ThemeUtils
import com.dilipsuthar.wallbox.utils.Tools

class AboutActivity : AppCompatActivity() {

    // View
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {

        when(ThemeUtils.getTheme(this)) {
            ThemeUtils.LIGHT -> setTheme(R.style.WallBox_Primary_Base_Light)
            ThemeUtils.DARK -> setTheme(R.style.WallBox_Primary_Base_Dark)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        ButterKnife.bind(this)

        customizeStatusBar()
        initToolbar()
    }

    private fun customizeStatusBar() {
        when(ThemeUtils.getTheme(this)) {
            ThemeUtils.LIGHT -> Tools.setSystemBarLight(this)
            ThemeUtils.DARK -> Tools.clearSystemBarLight(this)
        }

        Tools.setSystemBarColor(this, ThemeUtils.getThemeAttrColor(this, R.attr.colorPrimaryDark))
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title = resources.getString(R.string.toolbar_title_about)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.changeNavigationIconColor(toolbar, ThemeUtils.getThemeAttrColor(this, R.attr.colorAccent))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home)
            finish()

        return true
    }

}
