package com.dilipsuthar.wallbox.activity

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.adapters.SectionPagerAdapter
import com.dilipsuthar.wallbox.data_source.managers.AuthManager
import com.dilipsuthar.wallbox.data_source.model.User
import com.dilipsuthar.wallbox.data_source.service.Services
import com.dilipsuthar.wallbox.fragments.UserCollectionsFragment
import com.dilipsuthar.wallbox.fragments.UserLikedFragment
import com.dilipsuthar.wallbox.fragments.UserPhotosFragment
import com.dilipsuthar.wallbox.helpers.loadUrl
import com.dilipsuthar.wallbox.helpers.openWebView
import com.dilipsuthar.wallbox.preferences.Prefs
import com.dilipsuthar.wallbox.utils.PopupUtils
import com.dilipsuthar.wallbox.utils.ThemeUtils
import com.dilipsuthar.wallbox.utils.Tools
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.mikhaellopez.circularimageview.CircularImageView
import retrofit2.Call
import retrofit2.Response
/**
 * Created by DILIP SUTHAR 27/08/19
 */
class ProfileActivity : BaseActivity(), Services.OnRequestUserProfileListener {
    private val TAG = ProfileActivity::class.java.simpleName

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.img_user_profile) lateinit var imgUserProfile: CircularImageView
    @BindView(R.id.tv_username) lateinit var tvUsername: TextView
    @BindView(R.id.tv_followers_count) lateinit var tvFollowersCount: TextView
    @BindView(R.id.tv_following_count) lateinit var tvFollowingCount: TextView
    @BindView(R.id.tv_user_location) lateinit var tvUserLocation: TextView
    @BindView(R.id.tv_user_website) lateinit var tvUserWebsite: TextView
    @BindView(R.id.tv_profile_bio) lateinit var tvProfileBio: TextView
    @BindView(R.id.tab_layout) lateinit var mTabLayout: TabLayout
    @BindView(R.id.view_pager) lateinit var mViewpager: ViewPager
    //@BindView(R.id.chip_grp_interest) lateinit var chipGrpInterest: ChipGroup
    //@BindView(R.id.lyt_interest_chips) lateinit var lytInterestChips: LinearLayout

    private var mUser: User? = null
    private var mService: Services? = null

    private lateinit var mViewPagerAdapter: SectionPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        ButterKnife.bind(this)

        if (intent.extras != null && intent.getStringExtra(Prefs.USER) != null) {
            Log.d(TAG, "User: !null")
            mUser = Gson().fromJson(intent.getStringExtra(Prefs.USER), User::class.java)

            /** Set user data info */
            setData()
        } else {
            Log.d(TAG, "User: null")
            mUser = User()
            mUser?.username = AuthManager.getInstance().getUsername()
        }

        /** Service / API request */
        // Request user's public profile
        mService = Services.getService()
        mService?.requestUserProfile(mUser?.username!!, this)

        initToolbar()
        initComponent()
        //initTabLayout()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        Tools.changeMenuIconColor(menu!!, ThemeUtils.getThemeAttrColor(this, R.attr.tabUnselectedColor))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_share -> {
                ShareCompat.IntentBuilder.from(this)
                    .setText(mUser?.links?.html ?: "no link")
                    .setChooserTitle("Share profile link")
                    .setType("*.*")
                    .startChooser()
            }
            R.id.action_unsplash_profile -> {
                if (mUser?.links?.html != null) {
                    openWebView(mUser?.links?.html!!)
                }
            }
        }
        return true
    }

    /** @method init toolbar settings */
    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.title = ""
            //it.subtitle = "@${mUser?.username}"
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            it.setDisplayHomeAsUpEnabled(true)
        }
        Tools.changeNavigationIconColor(toolbar, ContextCompat.getColor(this, R.color.colorAccent))
    }

    private fun initComponent() {
    }

    private fun setData() {
        mUser?.let {
            imgUserProfile.loadUrl(
                it.profile_image.large,
                ContextCompat.getDrawable(this@ProfileActivity, R.drawable.placeholder_profile),
                ContextCompat.getDrawable(this@ProfileActivity, R.drawable.placeholder_profile))

            tvUsername.text = it.first_name ?: "" + " " + it.last_name ?: ""

            tvUserLocation.text = it.location ?: "-"
            tvUserWebsite.text = it.portfolio_url ?: "-"

            if (!TextUtils.isEmpty(it.bio))
                tvProfileBio.text = it.bio
            else Tools.inVisibleViews(tvProfileBio, type = Tools.GONE)

            // Set followers & following counts
            tvFollowersCount.text = Tools.formatLongNumbers(it.followers_count?.toLong() ?: 0)
            tvFollowingCount.text = Tools.formatLongNumbers(it.following_count?.toLong() ?: 0)
        }
    }

    /** @method init tab layout settings */
    private fun initTabLayout() {
        /** Set mViewPager and link it with TabLayout */
        mViewPagerAdapter = SectionPagerAdapter(supportFragmentManager)

        val userPhotoFragment = UserPhotosFragment.newInstance("latest")
        userPhotoFragment.setUser(mUser!!)

        val userLikedFragment = UserLikedFragment.newInstance("latest")
        userLikedFragment.setUser(mUser!!)

        val userCollectionsFragment = UserCollectionsFragment.newInstance("featured")
        userCollectionsFragment.setUser(mUser)

        with(mViewPagerAdapter) {
            addFragment(userPhotoFragment, "${mUser?.total_photos ?: 0} ${resources.getString(R.string.tab_title_user_photos_fragment)}")
            addFragment(userLikedFragment, "${mUser?.total_likes ?: 0} ${resources.getString(R.string.tab_title_user_liked_fragment)}")
            addFragment(userCollectionsFragment, "${mUser?.total_collections ?: 0} ${resources.getString(R.string.tab_title_user_collections_fragment)}")
            mViewpager.adapter = this
        }
        mViewpager.offscreenPageLimit = 2
        mTabLayout.setupWithViewPager(mViewpager)
    }

    override fun onRequestUserProfileSuccess(call: Call<User>, response: Response<User>) {
        if (response.isSuccessful) {
            mUser = response.body()
            Log.d(TAG, mUser.toString())

            mUser?.let { user ->

                // Set interest chips
                /*if (user.tags.custom.isNotEmpty()) {
                    for (tag in user.tags.custom) {
                        val chip = Chip(this@UserActivity)
                        chip.text = tag.title
                        chip.isClickable = true
                        chip.setOnClickListener {
                            val intent = Intent(this@UserActivity, SearchActivity::class.java)
                            intent.putExtra("keyword", chip.text.toString())
                            startActivity(intent)
                        }
                        chip.chipBackgroundColor = ColorStateList.valueOf(ThemeUtils.getThemeAttrColor(this@UserActivity, R.attr.primaryTextColor))
                        chip.setTextColor(ThemeUtils.getThemeAttrColor(this@UserActivity, R.attr.colorPrimary))
                        chipGrpInterest.addView(chip)
                    }
                } else Tools.inVisibleViews(lytInterestChips, type = Tools.GONE)*/

                setData()
                initTabLayout()
            }
        } else if (response.code() == 403) {
            PopupUtils.showToast(this, "Unable to make request!")
        }
    }

    override fun onRequestUserProfileFailed(call: Call<User>, t: Throwable) {
        Log.e(TAG, t.message!!)
        PopupUtils.showToast(this, "Failed to load profile!")
        onBackPressed()
    }
}
