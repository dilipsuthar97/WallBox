package com.dilipsuthar.wallbox.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.helpers.loadUrl
import com.dilipsuthar.wallbox.utils.ThemeUtils
import com.dilipsuthar.wallbox.utils.Tools
import com.mikhaellopez.circularimageview.CircularImageView
/**
 * Created by DILIP SUTHAR 05/06/19
 */
class AboutActivity : BaseActivity(), View.OnClickListener {

    @BindView(R.id.img_me) lateinit var imgMe: CircularImageView
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.tv_app_version) lateinit var tvAppVersion: TextView
    @BindView(R.id.btn_github) lateinit var btnGitHub: View
    @BindView(R.id.btn_twitter) lateinit var btnTwitter: View
    @BindView(R.id.btn_instagram) lateinit var btnInsta: View
    @BindView(R.id.btn_medium) lateinit var btnMedium: View
    @BindView(R.id.btn_unsplash) lateinit var btnUnsplash: View
    @BindView(R.id.btn_policy) lateinit var btnPolicy: View
    @BindView(R.id.btn_open_source_license) lateinit var btnOpenSourceLicense: View
    @BindView(R.id.txt_bottom_msg) lateinit var txtBottomMsg: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        when(ThemeUtils.getTheme(this)) {
            ThemeUtils.LIGHT -> setTheme(R.style.WallBox_Primary_Base_Light)
            ThemeUtils.DARK -> setTheme(R.style.WallBox_Primary_Base_Dark)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        ButterKnife.bind(this)

        initToolbar()
        initComponent()
    }

    override fun onClick(view: View?) {
        var url = ""
        when (view?.id) {
            R.id.btn_github -> url = "https://github.com/dilipsuthar1997"
            R.id.btn_twitter -> url = "https://twitter.com/dilipsuthar97"
            R.id.btn_instagram -> url = "https://instagram.com/dilipsuthar97"
            R.id.btn_medium -> url = "https://medium.com/@dilipsuthar97"
            R.id.btn_unsplash -> url = WallBox.UNSPLASH_URL
            R.id.btn_policy -> url = "https://wallbox.flycricket.io/privacy.html"
            R.id.btn_open_source_license -> {
                startActivity(Intent(this, OpenSourceLicensesActivity::class.java))
            }
        }

        if (view?.id != R.id.btn_open_source_license) {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(i)
        }
    }

    /** @method init toolbar settings */
    private fun initToolbar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title = resources.getString(R.string.toolbar_title_about)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.changeNavigationIconColor(toolbar, ThemeUtils.getThemeAttrColor(this, R.attr.colorAccent))
    }

    /** @method init all components */
    private fun initComponent() {
        imgMe.loadUrl("https://avatars1.githubusercontent.com/u/35637341?s=460&v=4",
            getDrawable(R.drawable.placeholder_profile),
            getDrawable(R.drawable.placeholder_profile))

        tvAppVersion.text = "v${Tools.getAppVersion(this)}"

        btnGitHub.setOnClickListener(this)
        btnTwitter.setOnClickListener(this)
        btnInsta.setOnClickListener(this)
        btnMedium.setOnClickListener(this)
        btnUnsplash.setOnClickListener(this)
        btnOpenSourceLicense.setOnClickListener(this)
        btnPolicy.setOnClickListener(this)

        txtBottomMsg.text = (StringBuilder()
            .append("Made with ")
            .append(String(Character.toChars(0x2764)))
            .append(" by ${resources.getString(R.string.owner_name)}"))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) onBackPressed()

        return true
    }

}
