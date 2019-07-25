package com.dilipsuthar.wallbox.activity

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.adapters.SectionPagerAdapter
import com.dilipsuthar.wallbox.fragments.CollectionWallFragment
import com.dilipsuthar.wallbox.fragments.CuratedWallFragment
import com.dilipsuthar.wallbox.fragments.RecentWallFragment
import com.dilipsuthar.wallbox.preferences.Preferences
import com.dilipsuthar.wallbox.utils.ThemeUtils
import com.dilipsuthar.wallbox.utils.Tools
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_recent_wall.*

/**
 * Created by Dilip on 05/06/2019
 */

class HomeActivity : BaseActivity() {

    companion object {
        const val TAG: String = "debug_HomeActivity"
        lateinit var sharedPreferences: SharedPreferences

        var activityRestarted = false
    }

    // Views
    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar
    @BindView(R.id.tab_layout) lateinit var mTabLayout: TabLayout
    @BindView(R.id.view_pager) lateinit var mViewPager: ViewPager
    @BindView(R.id.nav_view) lateinit var mNavigationView: NavigationView
    @BindView(R.id.drawer_layout) lateinit var mDrawerLayout: DrawerLayout

    // Others
    lateinit var rootView: View
    lateinit var menuToolbar: Menu
    private var mViewPagerAdapter: SectionPagerAdapter? = null

    // For wallpaper sort menu
    private var mSortMenuList = arrayOf<String>("Latest", "Oldest", "Popular")
    private var mCheckedSortItem: Int = 0

    // Fragments
    private lateinit var recentWallFragment: RecentWallFragment
    private lateinit var curatedWallFragment: CuratedWallFragment
    private lateinit var collectionWallFragment: CollectionWallFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        /*when(ThemeUtils.getTheme(this)) {
            ThemeUtils.LIGHT -> setTheme(R.style.WallBox_Primary_Base_Light)
            ThemeUtils.DARK -> setTheme(R.style.WallBox_Primary_Base_Dark)
        }*/
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ButterKnife.bind(this)
        rootView = window.decorView.rootView    // Get rootView from activity

        customizeStatusBar()
        initToolbar()
        initComponent()
        initTabLayout()
        initNavigationDrawer()
    }

    private fun customizeStatusBar() {
        when(ThemeUtils.getTheme(this)) {
            ThemeUtils.LIGHT -> Tools.setSystemBarLight(this)
            ThemeUtils.DARK -> Tools.clearSystemBarLight(this)
        }
        Tools.setSystemBarColor(this, ThemeUtils.getThemeAttrColor(this, R.attr.colorPrimaryDark))
    }

    private fun initToolbar() {
        setSupportActionBar(mToolbar)
        val actionBar = supportActionBar
        actionBar?.title = resources.getString(R.string.app_name)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.changeNavigationIconColor(mToolbar, ThemeUtils.getThemeAttrColor(this, R.attr.tabUnselectedColor))
    }

    private fun initComponent() {
        Log.d(TAG, "initComponent: called")
        recentWallFragment = RecentWallFragment()
        curatedWallFragment = CuratedWallFragment()
        collectionWallFragment = CollectionWallFragment()
    }

    private fun initTabLayout() {
        // Set mViewPager and link it with TabLayout
        mViewPagerAdapter = SectionPagerAdapter(supportFragmentManager)
        mViewPagerAdapter?.let {
            it.addFragment(RecentWallFragment.newInstance("latest"), "Recent")
            it.addFragment(CuratedWallFragment.newInstance("latest"), "Curated")
            it.addFragment(CollectionWallFragment.newInstance("featured"), "Collections")
            mViewPager.adapter = it
        }
        mViewPager.offscreenPageLimit = 2
        mTabLayout.setupWithViewPager(mViewPager)

        // Add icons to TabLayout
        mTabLayout.getTabAt(0)?.setIcon(R.drawable.ic_tab_recent)
        mTabLayout.getTabAt(1)?.setIcon(R.drawable.ic_tab_curated)
        mTabLayout.getTabAt(2)?.setIcon(R.drawable.ic_tab_collection)

        // Set color to TabLayout icons
        mTabLayout.getTabAt(0)?.icon?.setColorFilter(
            ThemeUtils.getThemeAttrColor(this, R.attr.tabSelectedColor),
            PorterDuff.Mode.SRC_IN)
        mTabLayout.getTabAt(1)?.icon?.setColorFilter(
            ThemeUtils.getThemeAttrColor(this, R.attr.tabUnselectedColor),
            PorterDuff.Mode.SRC_IN)
        mTabLayout.getTabAt(2)?.icon?.setColorFilter(
            ThemeUtils.getThemeAttrColor(this, R.attr.tabUnselectedColor),
            PorterDuff.Mode.SRC_IN)

        mTabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.icon?.setColorFilter(
                    ThemeUtils.getThemeAttrColor(this@HomeActivity, R.attr.tabUnselectedColor),
                    PorterDuff.Mode.SRC_IN)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.icon?.setColorFilter(
                    ThemeUtils.getThemeAttrColor(this@HomeActivity, R.attr.tabSelectedColor),
                    PorterDuff.Mode.SRC_IN)
                //menuToolbar.findItem(R.id.action_sort).isVisible = tab?.position != 2
            }

        })

        /*// Link toggle with mDrawerLayout
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            mDrawerLayout,
            mToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }

        // Configure the mDrawerLayout layout to add listener and show icon on mToolbar
        drawerToggle.isDrawerIndicatorEnabled = false
        mDrawerLayout.setDrawerListener(drawerToggle)
        drawerToggle.syncState()*/
    }

    private fun initNavigationDrawer() {
        mNavigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_Favorites -> startActivity(Intent(applicationContext, FavoritesActivity::class.java))
                R.id.nav_settings -> startActivity(Intent(applicationContext, SettingsActivity::class.java))
                R.id.nav_about -> startActivity(Intent(applicationContext, AboutActivity::class.java))
                R.id.nav_support_us -> startActivity(Intent(applicationContext, SupportUsActivity::class.java))
                else -> showSnackBar(it.title.toString(), Snackbar.LENGTH_SHORT)
            }

            mDrawerLayout.closeDrawers()
            true
        }
    }

    private fun showSnackBar(message: String, duration: Int) {
        Snackbar.make(mDrawerLayout, message, duration).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_main, menu)
        menuToolbar = menu!!
        Tools.changeMenuIconColor(menu, ThemeUtils.getThemeAttrColor(this, R.attr.tabUnselectedColor))
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        // TODO: This is not working ->> make this work
        when (mViewPager.currentItem) {
            0 -> {
                menu?.findItem(R.id.menu_sort_recent_latest)?.isVisible = true
                menu?.findItem(R.id.menu_sort_recent_oldest)?.isVisible = true
                menu?.findItem(R.id.menu_sort_recent_popular)?.isVisible = true
                menu?.findItem(R.id.menu_sort_curated_latest)?.isVisible = false
                menu?.findItem(R.id.menu_sort_curated_oldest)?.isVisible = false
                menu?.findItem(R.id.menu_sort_curated_popular)?.isVisible = false
                menu?.findItem(R.id.menu_sort_collection_all)?.isVisible = false
                menu?.findItem(R.id.menu_sort_collection_featured)?.isVisible = false
                menu?.findItem(R.id.menu_sort_collection_curated)?.isVisible = false
            }
            1 -> {
                menu?.findItem(R.id.menu_sort_recent_latest)?.isVisible = false
                menu?.findItem(R.id.menu_sort_recent_oldest)?.isVisible = false
                menu?.findItem(R.id.menu_sort_recent_popular)?.isVisible = false
                menu?.findItem(R.id.menu_sort_curated_latest)?.isVisible = true
                menu?.findItem(R.id.menu_sort_curated_oldest)?.isVisible = true
                menu?.findItem(R.id.menu_sort_curated_popular)?.isVisible = true
                menu?.findItem(R.id.menu_sort_collection_all)?.isVisible = false
                menu?.findItem(R.id.menu_sort_collection_featured)?.isVisible = false
                menu?.findItem(R.id.menu_sort_collection_curated)?.isVisible = false
            }
            2 -> {
                menu?.findItem(R.id.menu_sort_recent_latest)?.isVisible = false
                menu?.findItem(R.id.menu_sort_recent_oldest)?.isVisible = false
                menu?.findItem(R.id.menu_sort_recent_popular)?.isVisible = false
                menu?.findItem(R.id.menu_sort_curated_latest)?.isVisible = false
                menu?.findItem(R.id.menu_sort_curated_oldest)?.isVisible = false
                menu?.findItem(R.id.menu_sort_curated_popular)?.isVisible = false
                menu?.findItem(R.id.menu_sort_collection_all)?.isVisible = true
                menu?.findItem(R.id.menu_sort_collection_featured)?.isVisible = true
                menu?.findItem(R.id.menu_sort_collection_curated)?.isVisible = true
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val transaction = supportFragmentManager.beginTransaction()

        when (item?.itemId) {
            R.id.action_search -> showSnackBar(item.title.toString(), Snackbar.LENGTH_SHORT)
            R.id.action_sort -> {}
            R.id.menu_sort_recent_latest -> {
                transaction.replace(R.id.recent_container, RecentWallFragment.newInstance("latest")).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                showSnackBar("Wallpaper sorted by latest", Snackbar.LENGTH_SHORT)
            }
            R.id.menu_sort_recent_oldest -> {
                transaction.replace(R.id.recent_container, RecentWallFragment.newInstance("oldest")).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                showSnackBar("Wallpaper sorted by oldest", Snackbar.LENGTH_SHORT)
            }
            R.id.menu_sort_recent_popular -> {
                transaction.replace(R.id.recent_container, RecentWallFragment.newInstance("popular")).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                showSnackBar("Wallpaper sorted by popular", Snackbar.LENGTH_SHORT)
            }
            else -> mDrawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item!!)
    }

    private fun restartActivity() {
        val intent = this.intent
        this.finish()
        startActivity(intent)
        activityRestarted = true
    }

    private fun handleSortMenuItems(vararg value: Boolean, menu: Menu?) {
        for ((i, v) in value.withIndex()) {
            when (i) {
                0 -> menu?.findItem(R.id.menu_sort_recent_latest)?.isVisible = v
                1 -> menu?.findItem(R.id.menu_sort_recent_oldest)?.isVisible = v
                2 -> menu?.findItem(R.id.menu_sort_recent_popular)?.isVisible = v
                3 -> menu?.findItem(R.id.menu_sort_curated_latest)?.isVisible = v
                4 -> menu?.findItem(R.id.menu_sort_curated_oldest)?.isVisible = v
                5 -> menu?.findItem(R.id.menu_sort_curated_popular)?.isVisible = v
                6 -> menu?.findItem(R.id.menu_sort_collection_all)?.isVisible = v
                7 -> menu?.findItem(R.id.menu_sort_collection_featured)?.isVisible = v
                8 -> menu?.findItem(R.id.menu_sort_collection_curated)?.isVisible = v
            }
        }
    }

}
