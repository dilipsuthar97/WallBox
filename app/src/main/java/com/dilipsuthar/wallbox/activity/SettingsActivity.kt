package com.dilipsuthar.wallbox.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.danimahardhika.android.helpers.core.FileHelper
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.adapters.SettingAdapter
import com.dilipsuthar.wallbox.items.Setting
import com.dilipsuthar.wallbox.preferences.Preferences
import com.dilipsuthar.wallbox.utils.ThemeUtils
import com.dilipsuthar.wallbox.utils.Tools
import java.text.DecimalFormat

/**
 * Created by,
 * @author DILIP SUTHAR 05/06/19
 */

class SettingsActivity : BaseActivity() {

    private var activityRestarted = false
    private var mSettingList: ArrayList<Setting> = ArrayList()

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.recycler_view_setting) lateinit var mRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        ButterKnife.bind(this)

        initToolbar()
        initSettings()
    }

    /** @method init toolbar settings */
    private fun initToolbar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title = resources.getString(R.string.toolbar_title_settings)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.changeNavigationIconColor(toolbar, ContextCompat.getColor(applicationContext, R.color.colorAccent))
    }

    /** @method init setting menus */
    private fun initSettings() {
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.setHasFixedSize(true)

        val formatter = DecimalFormat("#0.00")
        val cacheSize = (FileHelper.getDirectorySize(applicationContext.cacheDir) / FileHelper.MB).toDouble()

        mSettingList.add(Setting(
            Setting.Type.LANGUAGE,
                resources.getString(R.string.title_language),
                Preferences.getSharedPreferences(this)?.getString(Preferences.LANGUAGE, "English")!!
            ))

        mSettingList.add(Setting(
            Setting.Type.THEME,
            resources.getString(R.string.title_Theme),
            resources.getString(R.string.desc_theme),
            checkState = if (ThemeUtils.getTheme(this) == ThemeUtils.DARK) 1 else 0
        ))

        mSettingList.add(Setting(
            Setting.Type.CLEAR_CACHE,
            resources.getString(R.string.title_clear_cache),
            resources.getString(R.string.desc_clear_cache),
            resources.getString(R.string.total_cache) + " ${formatter.format(cacheSize)} MB"
        ))

        mSettingList.add(Setting(
            type = Setting.Type.WALLPAPER_PREVIEW_QUALITY,
            title = resources.getString(R.string.title_wallpaper_preview_quality),
            subTitle = Preferences.getSharedPreferences(this)?.getString(Preferences.WALLPAPER_QUALITY, WallBox.DEFAULT_WALLPAPER_QUALITY)!!
        ))

        mSettingList.add(Setting(
            Setting.Type.WALLPAPER_SAVE_LOCATION,
            resources.getString(R.string.title_wallpaper_save_location),
            WallBox.DOWNLOAD_PATH
        ))

        mSettingList.add(Setting(
            Setting.Type.RESET_TUTORIAL,
            resources.getString(R.string.title_reset_tutorial),
            resources.getString(R.string.desc_reset_tutorial)
        ))

        mSettingList.add(Setting(
            Setting.Type.UNSPLASH_SITE,
            resources.getString(R.string.title_official_site),
            WallBox.UNSPLASH_URL
        ))

        mSettingList.add(Setting(Setting.Type.FOOTER))

        mRecyclerView.adapter = SettingAdapter(mSettingList, this, this@SettingsActivity)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    /*fun restartActivity() {
        val intent = Intent(this@SettingsActivity, SettingsActivity::class.java)
        startActivity(intent)
        finish()
        activityRestarted = true
    }*/

    companion object {
        fun get(): SettingsActivity {
            return SettingsActivity()
        }
    }

}
