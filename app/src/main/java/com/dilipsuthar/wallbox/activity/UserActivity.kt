package com.dilipsuthar.wallbox.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.data.model.User
import com.dilipsuthar.wallbox.helpers.loadUrl
import com.dilipsuthar.wallbox.preferences.Preferences
import com.dilipsuthar.wallbox.utils.Tools
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import com.mikhaellopez.circularimageview.CircularImageView

class UserActivity : BaseActivity() {

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.img_user_profile) lateinit var imgUserProfile: CircularImageView
    @BindView(R.id.tv_user_location) lateinit var tvUserLocation: TextView
    @BindView(R.id.tv_user_website) lateinit var tvUserWebsite: TextView
    @BindView(R.id.chip_grp_interest) lateinit var chipGrpInterest: ChipGroup
    @BindView(R.id.tv_profile_bio) lateinit var tvProfileBio: TextView

    private var mUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        ButterKnife.bind(this)
        mUser = Gson().fromJson(intent.getStringExtra(Preferences.USER), User::class.java)

        /** Set user info */
        mUser?.let {
            imgUserProfile.loadUrl(
                it.profile_image.large,
                R.drawable.placeholder_profile,
                R.drawable.placeholder_profile)

            tvUserLocation.text = if (it.location != "") it.location else "--"
            tvUserWebsite.text = if (it.portfolio_url != "") it.portfolio_url else "--"

            if (it.bio != "")
                tvProfileBio.text = it.bio
            else Tools.inVisibleViews(tvProfileBio, type = Tools.GONE)
        }

        initToolbar()
        initComponent()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.title = mUser?.first_name + " ${mUser?.last_name}"
            it.subtitle = "@${mUser?.username}"
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            it.setDisplayHomeAsUpEnabled(true)
        }
        Tools.changeNavigationIconColor(toolbar, ContextCompat.getColor(this, R.color.colorAccent))
    }

    private fun initComponent() {

    }
}
