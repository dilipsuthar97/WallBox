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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
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
import com.dilipsuthar.wallbox.utils.Popup
import com.dilipsuthar.wallbox.utils.ThemeUtils
import com.dilipsuthar.wallbox.utils.Tools
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
        var fabScrollUp: FloatingActionButton? = null
    }

    // VIEWS
    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar
    @BindView(R.id.tab_layout) lateinit var mTabLayout: TabLayout
    @BindView(R.id.view_pager) lateinit var mViewPager: ViewPager
    @BindView(R.id.nav_view) lateinit var mNavigationView: NavigationView
    @BindView(R.id.drawer_layout) lateinit var mDrawerLayout: DrawerLayout
    @BindView(R.id.root_coordinator_layout) lateinit var mRootView: CoordinatorLayout
//    @BindView(R.id.fab_scroll_to_top) lateinit var mFabScrollUp: FloatingActionButton

    // VARS
    private var mViewPagerAdapter: SectionPagerAdapter? = null

    // Wallpaper sort menu
    private var mSortRecentLatest: MenuItem? = null
    private var mSortRecentOldest: MenuItem? = null
    private var mSortRecentPopular: MenuItem? = null
    private var mSortCuratedLatest: MenuItem? = null
    private var mSortCuratedOldest: MenuItem? = null
    private var mSortCuratedPopular: MenuItem? = null
    private var mSortCollectionAll: MenuItem? = null
    private var mSortCollectionFeatured: MenuItem? = null
    private var mSortCollectionCurated: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ButterKnife.bind(this)
//        fabScrollUp = mFabScrollUp

        initToolbar()
        initTabLayout()
        initNavigationDrawer()

        /** Fab scrollToUp listener */
        /*mFabScrollUp.setOnClickListener {
            val fragment: Fragment?
            when (mViewPager.currentItem) {
                0 -> {
                    fragment = mViewPagerAdapter?.getItem(0)
                    if (fragment is RecentWallFragment)
                        (fragment as RecentWallFragment).scrollToTop()
                }
            }
        }*/
    }

    private fun initToolbar() {
        setSupportActionBar(mToolbar)
        val actionBar = supportActionBar
        actionBar?.title = resources.getString(R.string.app_name)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.changeNavigationIconColor(mToolbar, ThemeUtils.getThemeAttrColor(this, R.attr.tabUnselectedColor))
    }

    private fun initTabLayout() {
        /** Set mViewPager and link it with TabLayout */
        mViewPagerAdapter = SectionPagerAdapter(supportFragmentManager)
        mViewPagerAdapter?.let {
            it.addFragment(RecentWallFragment.newInstance("latest"), "Fresh")
            it.addFragment(CuratedWallFragment.newInstance("latest"), "Curated")
            it.addFragment(CollectionWallFragment.newInstance("featured"), "Collections")
            mViewPager.adapter = it
        }
        mViewPager.offscreenPageLimit = 2
        mTabLayout.setupWithViewPager(mViewPager)

        /** Add icons to TabLayout */
        mTabLayout.getTabAt(0)?.setIcon(R.drawable.ic_tab_recent)
        mTabLayout.getTabAt(1)?.setIcon(R.drawable.ic_tab_curated)
        mTabLayout.getTabAt(2)?.setIcon(R.drawable.ic_tab_collection)

        /** Set color to TabLayout icons */
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
        Tools.changeMenuIconColor(menu!!, ThemeUtils.getThemeAttrColor(this, R.attr.tabUnselectedColor))
        mSortRecentLatest = menu.findItem(R.id.menu_sort_recent_latest)
        mSortRecentOldest = menu.findItem(R.id.menu_sort_recent_oldest)
        mSortRecentPopular = menu.findItem(R.id.menu_sort_recent_popular)
        mSortCuratedLatest = menu.findItem(R.id.menu_sort_curated_latest)
        mSortCuratedOldest = menu.findItem(R.id.menu_sort_curated_oldest)
        mSortCuratedPopular = menu.findItem(R.id.menu_sort_curated_popular)
        mSortCollectionAll = menu.findItem(R.id.menu_sort_collection_all)
        mSortCollectionFeatured = menu.findItem(R.id.menu_sort_collection_featured)
        mSortCollectionCurated = menu.findItem(R.id.menu_sort_collection_curated)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val transaction = supportFragmentManager.beginTransaction()

        when (item?.itemId) {
            R.id.action_search -> showSnackBar(item.title.toString(), Snackbar.LENGTH_SHORT)
            R.id.action_sort -> {
                when (mViewPager.currentItem) {
                    0 -> handleSortMenuItems(true,true,true,false,false,false,false,false,false)
                    1 -> handleSortMenuItems(false,false,false,true,true,true,false,false,false)
                    2 -> handleSortMenuItems(false,false,false,false,false,false,true,true,true)
                }
            }
            R.id.menu_sort_recent_latest -> {
                transaction.replace(R.id.recent_container, RecentWallFragment.newInstance("latest")).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                showSnackBar("Wallpaper sorted by Latest", Snackbar.LENGTH_SHORT)
            }
            R.id.menu_sort_recent_oldest -> {
                transaction.replace(R.id.recent_container, RecentWallFragment.newInstance("oldest")).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                showSnackBar("Wallpaper sorted by Oldest", Snackbar.LENGTH_SHORT)
            }
            R.id.menu_sort_recent_popular -> {
                transaction.replace(R.id.recent_container, RecentWallFragment.newInstance("popular")).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                showSnackBar("Wallpaper sorted by Popular", Snackbar.LENGTH_SHORT)
            }
            R.id.menu_sort_curated_latest -> {
                transaction.replace(R.id.curated_container, CuratedWallFragment.newInstance("latest")).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                showSnackBar("Wallpaper sorted by Latest", Snackbar.LENGTH_SHORT)
            }
            R.id.menu_sort_curated_oldest -> {
                transaction.replace(R.id.curated_container, CuratedWallFragment.newInstance("oldest")).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                showSnackBar("Wallpaper sorted by Oldest", Snackbar.LENGTH_SHORT)
            }
            R.id.menu_sort_curated_popular -> {
                transaction.replace(R.id.curated_container, CuratedWallFragment.newInstance("popular")).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                showSnackBar("Wallpaper sorted by Popular", Snackbar.LENGTH_SHORT)
            }
            R.id.menu_sort_collection_all -> {
                transaction.replace(R.id.collections_container, CollectionWallFragment.newInstance("all")).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                showSnackBar("Collections sorted by All", Snackbar.LENGTH_SHORT)
            }
            R.id.menu_sort_collection_featured -> {
                transaction.replace(R.id.collections_container, CollectionWallFragment.newInstance("featured")).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                showSnackBar("Collections sorted by All", Snackbar.LENGTH_SHORT)
            }
            R.id.menu_sort_collection_curated -> {
                transaction.replace(R.id.collections_container, CollectionWallFragment.newInstance("curated")).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                showSnackBar("Collections sorted by All", Snackbar.LENGTH_SHORT)
            }
            else -> mDrawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item!!)
    }

    private fun handleSortMenuItems(vararg value: Boolean) {
        for ((i, v) in value.withIndex()) {
            when (i) {
                0 -> mSortRecentLatest?.isVisible = v
                1 -> mSortRecentOldest?.isVisible = v
                2 -> mSortRecentPopular?.isVisible = v
                3 -> mSortCuratedLatest?.isVisible = v
                4 -> mSortCuratedOldest?.isVisible = v
                5 -> mSortCuratedPopular?.isVisible = v
                6 -> mSortCollectionAll?.isVisible = v
                7 -> mSortCollectionFeatured?.isVisible = v
                8 -> mSortCollectionCurated?.isVisible = v
            }
        }
    }

}
