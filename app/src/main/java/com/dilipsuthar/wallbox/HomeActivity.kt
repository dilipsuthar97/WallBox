package com.dilipsuthar.wallbox

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.adapters.SectionPagerAdapter
import com.dilipsuthar.wallbox.fragments.CollectionWallFragment
import com.dilipsuthar.wallbox.fragments.CuratedWallFragment
import com.dilipsuthar.wallbox.fragments.RecentWallFragment
import com.dilipsuthar.wallbox.preferences.Preferences
import com.dilipsuthar.wallbox.utils.Tools
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout

/**
 * Created by Dilip on 05/06/2019
 */

class HomeActivity : AppCompatActivity() {

    companion object {
        const val TAG: String = "debug_HomeActivity"
        lateinit var sharedPreferences: SharedPreferences
    }

    // Views
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.tab_layout) lateinit var tabLayout: TabLayout
    @BindView(R.id.view_pager) lateinit var viewPager: ViewPager
    @BindView(R.id.nav_view) lateinit var navigationView: NavigationView
    @BindView(R.id.drawer_layout) lateinit var drawerLayout: DrawerLayout

    // Others
    lateinit var rootView: View
    lateinit var menuToolbar: Menu
    lateinit var sortPopupMenu: PopupMenu

    // For wallpaper sort menu
    private var sortItems = arrayOf<String>("Latest", "Oldest", "Popular")
    private var chckedSortItem: Int = 0

    // Fragments
    private lateinit var recentWallFragment: RecentWallFragment
    private lateinit var curatedWallFragment: CuratedWallFragment
    private lateinit var collectionWallFragment: CollectionWallFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility = 0
        }

        Tools.setSystemBarColor(this, R.color.colorPrimary)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title = resources.getString(R.string.app_name)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.changeNavigationIconColor(toolbar, resources.getColor(R.color.colorAccent))
    }

    private fun initComponent() {
        Log.d(TAG, "initComponent: called")
        recentWallFragment = RecentWallFragment()
        curatedWallFragment = CuratedWallFragment()
        collectionWallFragment = CollectionWallFragment()
    }

    private fun initTabLayout() {
        // Set viewPager and link it with TabLayout
        setupViewPager(viewPager)
        tabLayout.setupWithViewPager(viewPager)

        // Add icons to TabLayout
        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_tab_recent)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_tab_curated)
        tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_tab_collection)

        // Set color to TabLayout icons
        tabLayout.getTabAt(0)?.getIcon()?.setColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN)
        tabLayout.getTabAt(1)?.getIcon()?.setColorFilter(resources.getColor(R.color.colorSecondary), PorterDuff.Mode.SRC_IN)
        tabLayout.getTabAt(2)?.getIcon()?.setColorFilter(resources.getColor(R.color.colorSecondary), PorterDuff.Mode.SRC_IN)

        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.getIcon()?.setColorFilter(resources.getColor(R.color.colorSecondary), PorterDuff.Mode.SRC_ATOP)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.getIcon()?.setColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP)
                menuToolbar.findItem(R.id.action_sort).isVisible = tab?.position != 2
            }

        })

        /*// Link toggle with drawerLayout
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }

        // Configure the drawerLayout layout to add listener and show icon on toolbar
        drawerToggle.isDrawerIndicatorEnabled = false
        drawerLayout.setDrawerListener(drawerToggle)
        drawerToggle.syncState()*/
    }

    private fun initNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_Favorites -> startActivity(Intent(applicationContext, FavoritesActivity::class.java))
                R.id.nav_settings -> startActivity(Intent(applicationContext, SettingsActivity::class.java))
                R.id.nav_about -> startActivity(Intent(applicationContext, AboutActivity::class.java))
                else -> showSnackbar(it.title.toString(), Snackbar.LENGTH_SHORT)
            }

            drawerLayout.closeDrawers()
            return@setNavigationItemSelectedListener true
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        Log.d(TAG, "setupViewPager: called")

        val viewPagerAdapter = SectionPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(recentWallFragment, "Recent")
        viewPagerAdapter.addFragment(curatedWallFragment, "Curated")
        viewPagerAdapter.addFragment(collectionWallFragment, "Collection")
        viewPager.adapter = viewPagerAdapter
    }

    private fun showSnackbar(message: String, duration: Int) {
        Snackbar.make(drawerLayout, message, duration).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_main, menu)
        menuToolbar = menu!!
        Tools.changeMenuIconColor(menu, resources.getColor(R.color.colorAccent))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_search -> showSnackbar(item.title.toString(), Snackbar.LENGTH_SHORT)
            R.id.action_sort -> {
                val menuItemView = findViewById<View>(R.id.action_sort)

                // Create sort popup menu here
                /*val sortPopupMenu = PopupMenu(this, menuItemView)
                sortPopupMenu.setOnMenuItemClickListener {

                    showSnackbar("Wallpaper sorted by ${it.title}", Snackbar.LENGTH_LONG)
                    true

                }
                sortPopupMenu.inflate(R.menu.menu_toolbar_sort)
                sortPopupMenu.show()*/

                // Create sort dialog menu
                chckedSortItem = sharedPreferences.getInt(Preferences.SORTING_WALLPAPERS, 0)
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Sort wallpapers")
                builder.setSingleChoiceItems(sortItems, chckedSortItem) {dialog, pos ->
                    chckedSortItem = pos
                }
                builder.setPositiveButton("OK") { dialog, which ->
                    sharedPreferences.edit().putInt(Preferences.SORTING_WALLPAPERS, chckedSortItem).apply()
                    showSnackbar("Wallpaper sorted by ${sortItems[chckedSortItem]}", Snackbar.LENGTH_LONG)
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    dialog.cancel()
                }
                builder.show()

            }
            else -> drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    /*var isToolbarHide = false
    private fun hideToolbar(hide: Boolean) {
        if (hide && isToolbarHide || !hide && !isToolbarHide) return
        isToolbarHide = hide
        val moveToolbarY = if (hide) -(2 * toolbar.height) else 0
        val moveTabLytY = if (hide) -(tabLayout.height) else 0
        toolbar.animate().translationY(moveToolbarY.toFloat()).setStartDelay(100).setDuration(300).start()
        tabLayout.animate().translationY(moveTabLytY.toFloat()).setStartDelay(100).setDuration(300).start()

    }*/

}
