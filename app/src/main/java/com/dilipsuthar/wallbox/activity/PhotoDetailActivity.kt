package com.dilipsuthar.wallbox.activity

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.data.model.Photo
import com.dilipsuthar.wallbox.data.model.PhotoStatistics
import com.dilipsuthar.wallbox.data.service.Services
import com.dilipsuthar.wallbox.helpers.eq
import com.dilipsuthar.wallbox.helpers.getFormattedNumber
import com.dilipsuthar.wallbox.helpers.isDark
import com.dilipsuthar.wallbox.helpers.loadUrl
import com.dilipsuthar.wallbox.preferences.Preferences
import com.dilipsuthar.wallbox.utils.*
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.mikhaellopez.circularimageview.CircularImageView
import retrofit2.Call
import retrofit2.Response

/**
 * Created by,
 * @author DILIP SUTHAR 05/06/19
 */

class PhotoDetailActivity : BaseActivity() {
    private val TAG = "WallBox.PhotoDetailAct"

    private var mService: Services? = null
    private var mOnRequestPhotoListener: Services.OnRequestPhotoListener? = null
    private var mOnRequestPhotoStatistics: Services.OnRequestPhotoStatistics? = null
    private lateinit var mPhoto: Photo
    private lateinit var mPhotoStatistics: PhotoStatistics

    private var sharedPreferences: SharedPreferences? = null

    @BindView(R.id.img_photo) lateinit var imgPhoto: PhotoView
    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar
    @BindView(R.id.img_user) lateinit var imgUser: CircularImageView
    @BindView(R.id.tv_photo_by) lateinit var tvPhotoBy: TextView
    @BindView(R.id.root_view_bottom_sheet) lateinit var bottomSheet: View

    @BindView(R.id.tv_description) lateinit var tvDescription: TextView
    @BindView(R.id.tv_likes) lateinit var tvLikes: TextView
    @BindView(R.id.tv_downloads) lateinit var tvDownload: TextView
    @BindView(R.id.tv_views) lateinit var tvViews: TextView
    @BindView(R.id.tv_location) lateinit var tvLocation: TextView
    @BindView(R.id.tv_color) lateinit var tvColor: TextView
    @BindView(R.id.view_color) lateinit var viewColor: CardView
    @BindView(R.id.btn_set_wallpaper) lateinit var btnSetWallpaper: ImageButton
    @BindView(R.id.btn_download) lateinit var btnDownload: ImageButton

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)
        ButterKnife.bind(this)
        sharedPreferences = Preferences.getSharedPreferences(this)
        mPhoto = Gson().fromJson(intent.getStringExtra(Preferences.PHOTO), Photo::class.java)

        /** Set mPhoto data */
        imgPhoto.setBackgroundColor(Color.parseColor("#000000"))

        val url = when (sharedPreferences?.getString(Preferences.WALLPAPER_QUALITY, WallBox.DEFAULT_WALLPAPER_QUALITY)) {
            "Full" -> mPhoto.urls.full
            "Regular" -> mPhoto.urls.regular
            "Small" -> mPhoto.urls.small
            else -> mPhoto.urls.thumb
        }
        imgPhoto.loadUrl(url)
        imgUser.loadUrl(
            mPhoto.user.profile_image.large,
            R.drawable.placeholder_profile,
            R.drawable.placeholder_profile)

        if (mPhoto.user.first_name != "" && mPhoto.user.last_name != "")
            tvPhotoBy.text = mPhoto.user.first_name + " " + mPhoto.user.last_name
        else if (mPhoto.user.first_name != "")
            tvPhotoBy.text = mPhoto.user.first_name
        else if (mPhoto.user.username != "")
            tvPhotoBy.text = mPhoto.user.first_name

        when {
            mPhoto.description != "" -> tvDescription.text = mPhoto.description
            mPhoto.alt_description != "" -> tvDescription.text = mPhoto.alt_description
            else -> tvDescription.text = resources.getString(R.string.desc_no_description)
        }

        tvColor.text = mPhoto.color
        viewColor.setCardBackgroundColor(Color.parseColor(mPhoto.color))

        /** Service / API request */
        // request Photo
        mService = Services.getService()
        mOnRequestPhotoListener = object : Services.OnRequestPhotoListener {
            override fun onRequestPhotoSuccess(call: Call<Photo>, response: Response<Photo>) {
                if (response.isSuccessful) {
                    Log.d(TAG, Gson().toJson(response.body()))
                    mPhoto = response.body()!!
                    setPhotoInfo()
                }
            }

            override fun onRequestPhotoFailed(call: Call<Photo>, t: Throwable) {
                Log.d(TAG, t.message!!)
                PopupUtils.showToast(applicationContext, resources.getString(R.string.photo_error), Toast.LENGTH_SHORT)
            }
        }
        mService?.requestPhoto(mPhoto.id, mOnRequestPhotoListener)

        // request Photo Statistics
        mOnRequestPhotoStatistics = object : Services.OnRequestPhotoStatistics {
            override fun onRequestSuccess(call: Call<PhotoStatistics>, response: Response<PhotoStatistics>) {
                if (response.isSuccessful) {
                    Log.d(TAG, Gson().toJson(response.body()!!))
                    mPhotoStatistics = response.body()!!
                    setStatisticsData()
                }
            }

            override fun onRequestFailed(call: Call<PhotoStatistics>, t: Throwable) {
                Log.d(TAG, t.message!!)
                PopupUtils.showToast(applicationContext, resources.getString(R.string.photo_statistics_error), Toast.LENGTH_SHORT)
            }
        }
        mService?.requestPhotoStatistics(mPhoto.id, mOnRequestPhotoStatistics)

        /** Bottom sheet */
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, offset: Float) {
                Log.d(TAG, offset.toString())
            }

            override fun onStateChanged(p0: View, newState: Int) {

                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheet.setBackgroundColor((ContextCompat.getColor(applicationContext, R.color.overlay_dark_50)))
                    tvPhotoBy.setTextColor(ContextCompat.getColor(applicationContext, R.color.primaryTextColor_night))
                    btnSetWallpaper.setColorFilter(ContextCompat.getColor(applicationContext, R.color.primaryTextColor_night))
                    btnDownload.setColorFilter(ContextCompat.getColor(applicationContext, R.color.primaryTextColor_night))
                } else {
                    bottomSheet.setBackgroundColor(Color.parseColor(mPhoto.color))
                    if (Color.parseColor(mPhoto.color).isDark() eq true)
                        setViewsColor(
                            ContextCompat.getColor(applicationContext, R.color.primaryTextColor_night),
                            ContextCompat.getColor(applicationContext, R.color.secondaryTextColor_night))
                    else
                        setViewsColor(
                            ContextCompat.getColor(applicationContext, R.color.primaryTextColor),
                            ContextCompat.getColor(applicationContext, R.color.secondaryTextColor))
                }
            }
        })

        initToolbar()
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        hideSystemUI()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_photo_detail, menu)
        Tools.changeMenuIconColor(menu!!, ContextCompat.getColor(this, R.color.white))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_photo_info -> {
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                else
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            R.id.scale_center_crop -> imgPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
            R.id.scale_center -> imgPhoto.scaleType = ImageView.ScaleType.CENTER
            R.id.scale_fit_ceter -> imgPhoto.scaleType = ImageView.ScaleType.FIT_CENTER
            R.id.scale_fit_xy -> imgPhoto.scaleType = ImageView.ScaleType.FIT_XY
        }
        return super.onOptionsItemSelected(item)
    }

    /** @method init toolbar settings */
    private fun initToolbar() {
        setSupportActionBar(mToolbar)
        val actionBar = supportActionBar
        actionBar?.title = ""
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.changeNavigationIconColor(mToolbar, ContextCompat.getColor(this, R.color.white))
        Tools.setSystemBarColor(this, Color.TRANSPARENT)
    }

    /** @method hide system UI */
    private fun hideSystemUI() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        //or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    /** @method set photo statistics data **/
    private fun setStatisticsData() {
        tvLikes.text = mPhotoStatistics.likes.total.getFormattedNumber()
        tvDownload.text = mPhotoStatistics.downloads.total.getFormattedNumber()
        tvViews.text = mPhotoStatistics.views.total.getFormattedNumber()
    }

    /** @method set photo info */
    private fun setPhotoInfo() {
        if (mPhoto.location.city != null && mPhoto.location.country != null)
            tvLocation.text = "${mPhoto.location.city}, ${mPhoto.location.country}"
        else if(mPhoto.location.city != null) tvLocation.text = mPhoto.location.city
        else if (mPhoto.location.country != null) tvLocation.text = mPhoto.location.country
        else tvLocation.text = resources.getString(R.string.desc_no_location)
    }

    /** @method change photo detail's bottom sheet UI & colors */
    private fun setViewsColor(colorPrimary: Int, colorSecondary: Int) {
        (findViewById<TextView>(R.id.label_description)).setTextColor(colorPrimary)
        tvPhotoBy.setTextColor(colorPrimary)
        btnSetWallpaper.setColorFilter(colorPrimary)
        btnDownload.setColorFilter(colorPrimary)
        tvDescription.setTextColor(colorSecondary)
        (findViewById<ImageView>(R.id.ic_location)).setColorFilter(colorPrimary)
        tvLocation.setTextColor(colorSecondary)
        (findViewById<TextView>(R.id.label_info)).setTextColor(colorPrimary)
        (findViewById<ImageView>(R.id.ic_likes)).setColorFilter(colorPrimary)
        (findViewById<ImageView>(R.id.ic_downloads)).setColorFilter(colorPrimary)
        (findViewById<ImageView>(R.id.ic_views)).setColorFilter(colorPrimary)
        tvLikes.setTextColor(colorSecondary)
        tvDownload.setTextColor(colorSecondary)
        tvViews.setTextColor(colorSecondary)
        (findViewById<TextView>(R.id.label_color_palette)).setTextColor(colorPrimary)
        tvColor.setTextColor(colorSecondary)

    }

}
