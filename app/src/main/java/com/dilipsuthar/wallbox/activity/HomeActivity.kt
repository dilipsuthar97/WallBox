package com.dilipsuthar.wallbox.activity

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.MotionEventCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.adapters.SectionPagerAdapter
import com.dilipsuthar.wallbox.fragments.*
import com.dilipsuthar.wallbox.helpers.LocaleHelper
import com.dilipsuthar.wallbox.utils.ThemeUtils
import com.dilipsuthar.wallbox.utils.Tools
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import java.util.*
/**
 * Created by DILIP SUTHAR 05/06/19
 */
class HomeActivity : BaseActivity() {

    companion object {
        const val TAG: String = "debug_HomeActivity"
        fun get(): HomeActivity {
            return HomeActivity()
        }
    }

    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar
    @BindView(R.id.tab_layout) lateinit var mTabLayout: TabLayout
    @BindView(R.id.search_tab_layout) lateinit var mTabLayoutSearch: TabLayout
    @BindView(R.id.view_pager) lateinit var mViewPager: ViewPager
    @BindView(R.id.search_view_pager) lateinit var mViewPagerSearch: ViewPager
    @BindView(R.id.nav_view) lateinit var mNavigationView: NavigationView
    @BindView(R.id.drawer_layout) lateinit var mDrawerLayout: DrawerLayout
    @BindView(R.id.root_coordinator_layout) lateinit var mRootView: View
    /*@BindView(R.id.fab_scroll_to_top) lateinit var mFabScrollUp: FloatingActionButton*/
    @BindView(R.id.bottom_sheet_search_root) lateinit var bottomSheetSearch: View
    @BindView(R.id.et_search) lateinit var etSearch: EditText
    @BindView(R.id.btn_close_sheet) lateinit var btnCloseSheet: ImageButton
    @BindView(R.id.btn_speech_to_txt) lateinit var btnSpeechToTxt: ImageButton

    private var mViewPagerAdapter: SectionPagerAdapter? = null
    private var mViewPagerAdapterSearch: SectionPagerAdapter? = null
    private lateinit var currentLanguage: Locale
    private lateinit var currentTheme: String
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

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
        // fabScrollUp = mFabScrollUp

        currentLanguage = LocaleHelper.getLocale(this)
        currentTheme = ThemeUtils.getTheme(this)

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

        /** Bottom sheet */
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetSearch)
        bottomSheetBehavior.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                bottomSheet.setOnTouchListener { _, motionEvent ->

                    when (MotionEventCompat.getActionMasked(motionEvent)) {
                        MotionEvent.ACTION_DOWN -> false
                        else -> true
                    }
                }
            }
        })
        btnCloseSheet.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        /** Search edit text */
        etSearch.requestFocus()
        etSearch.setOnEditorActionListener { textView, i, keyEvent ->
            val transaction = supportFragmentManager.beginTransaction()

            val query = etSearch.text.toString()
            if (query.isNotEmpty()) {
                Log.d(TAG, query)
                /*transaction.replace(R.id.search_photo_fragment, SearchPhotoFragment.newInstance(query))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()*/

                setupSearchViewPager(query)
            }

            val view = this.currentFocus
            if (view != null) {
                val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }

            true
        }

    }

    override fun onResume() {
        super.onResume()
        if (currentLanguage != LocaleHelper.getLocale(this)) {
            recreate()
            mDrawerLayout.closeDrawers()
        } else if (currentTheme != ThemeUtils.getTheme(this)) {
            recreate()
            mDrawerLayout.closeDrawers()
        }
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        else super.onBackPressed()
    }

    /*override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleHelper.loadLocal(this)
        recreate()
    }*/

    override fun onDestroy() {
        super.onDestroy()
        //Services.getService().cancel()  // Cancel all request's call on Activity destroy
    }

    /** @method init toolbar settings */
    private fun initToolbar() {
        setSupportActionBar(mToolbar)
        val actionBar = supportActionBar
        actionBar?.title = resources.getString(R.string.app_name)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.changeNavigationIconColor(mToolbar, ThemeUtils.getThemeAttrColor(this, R.attr.tabUnselectedColor))
    }

    /** @method init tab layout settings */
    private fun initTabLayout() {
        // Main tab layout
        /** Set mViewPager and link it with TabLayout */
        mViewPagerAdapter = SectionPagerAdapter(supportFragmentManager)
        with(mViewPagerAdapter!!) {
            addFragment(RecentWallFragment.newInstance("latest"), resources.getString(R.string.tab_title_recent_wall_fragment))
            addFragment(CuratedWallFragment.newInstance("latest"), resources.getString(R.string.tab_title_curated_wall_fragment))
            addFragment(CollectionsFragment.newInstance("featured"), resources.getString(R.string.tab_title_collections_fragment))
            mViewPager.adapter = this
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

        // Search bottom sheet tab layout
        /** Set mViewPager and link it with TabLayout */
        setupSearchViewPager(null)

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

    /** @method init navigation drawer settings */
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

    /** @method show snack bar */
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
            R.id.action_search -> {
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                else
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
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
                transaction.replace(R.id.collections_container, CollectionsFragment.newInstance("all")).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                showSnackBar("Collections sorted by All", Snackbar.LENGTH_SHORT)
            }
            R.id.menu_sort_collection_featured -> {
                transaction.replace(R.id.collections_container, CollectionsFragment.newInstance("featured")).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                showSnackBar("Collections sorted by Featured", Snackbar.LENGTH_SHORT)
            }
            R.id.menu_sort_collection_curated -> {
                transaction.replace(R.id.collections_container, CollectionsFragment.newInstance("curated")).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                showSnackBar("Collections sorted by Curated", Snackbar.LENGTH_SHORT)
            }
            else -> mDrawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item!!)
    }

    /** @method handle sort menu items on different fragment */
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

    /**
     * It setup view pager and fragments for search query in bottom sheet
     *
     * @param query user input keyword for search
     */
    private fun setupSearchViewPager(query: String?) {
        /** Set mViewPager and link it with TabLayout */
        mViewPagerAdapterSearch = SectionPagerAdapter(supportFragmentManager)
        with(mViewPagerAdapterSearch!!) {
            addFragment(SearchPhotoFragment.newInstance(query), "Photo")
            addFragment(SearchCollectionFragment.newInstance(query), "Collection")
            addFragment(SearchUserFragment.newInstance(query), "User")
            mViewPagerSearch.adapter = this
        }
        mViewPagerSearch.offscreenPageLimit = 2
        mTabLayoutSearch.setupWithViewPager(mViewPagerSearch)
    }
}

