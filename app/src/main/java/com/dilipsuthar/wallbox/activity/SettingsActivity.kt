package com.dilipsuthar.wallbox.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.utils.ThemeUtils
import com.dilipsuthar.wallbox.utils.Tools

class SettingsActivity : BaseActivity() {

    // Vars
        private var activityRestarted = false
    private lateinit var sharedPreferences: SharedPreferences

    // Views
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.switch_theme) lateinit var mSwitchTheme: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        ButterKnife.bind(this)

        initToolbar()

        if (ThemeUtils.getTheme(this) == ThemeUtils.DARK) {
            mSwitchTheme.isChecked = true
        }

        mSwitchTheme.setOnCheckedChangeListener { compoundButton, value ->
            if (value) ThemeUtils.setTheme(this, ThemeUtils.DARK)
            else ThemeUtils.setTheme(this, ThemeUtils.LIGHT)

            restartActivity()
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title = resources.getString(R.string.toolbar_title_settings)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.changeNavigationIconColor(toolbar, ThemeUtils.getThemeAttrColor(this, R.attr.colorAccent))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home)
            finish()

        return true
    }

    private fun restartActivity() {
        val intent = Intent(this@SettingsActivity, SettingsActivity::class.java)
        startActivity(intent)
        finish()
        activityRestarted = true
    }

}
