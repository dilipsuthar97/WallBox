package com.dilipsuthar.wallbox.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.adapters.OpenSourceLicenseAdapter
import com.dilipsuthar.wallbox.data_source.DataGenerator
import com.dilipsuthar.wallbox.items.OpenSourceLicense
import com.dilipsuthar.wallbox.utils.ThemeUtils
import com.dilipsuthar.wallbox.utils.Tools

/**
 * Created by DILIP SUTHAR 07/09/19
 */
class OpenSourceLicensesActivity : BaseActivity() {

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.recycler_view) lateinit var mRecyclerView: RecyclerView

    private lateinit var adapter: OpenSourceLicenseAdapter
    private var listLicenses: ArrayList<OpenSourceLicense> = ArrayList()
    private lateinit var OnLicenseClickListener: OpenSourceLicenseAdapter.OnLicenseClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_source_licenses)
        ButterKnife.bind(this)

        initToolbar()
        initComponent()
    }

    /** @mthod init toolbar settings */
    private fun initToolbar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title = resources.getString(R.string.toolbar_title_open_source_licenses)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.changeNavigationIconColor(toolbar, ThemeUtils.getThemeAttrColor(this, R.attr.colorAccent))
    }

    /** @mtethos init all components */
    private fun initComponent() {
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.setHasFixedSize(true)

        listLicenses = DataGenerator.getLicenseData(this)
        OnLicenseClickListener = object : OpenSourceLicenseAdapter.OnLicenseClickListener {
            override fun onLicenseClick(openSourceLicense: OpenSourceLicense, view: View) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(openSourceLicense.url)))
            }
        }

        adapter = OpenSourceLicenseAdapter(listLicenses, OnLicenseClickListener)
        mRecyclerView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) onBackPressed()

        return true
    }
}
