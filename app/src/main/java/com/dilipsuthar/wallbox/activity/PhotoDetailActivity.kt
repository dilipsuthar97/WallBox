package com.dilipsuthar.wallbox.activity

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.data.model.Photo
import com.dilipsuthar.wallbox.data.model.PhotoStatistics
import com.dilipsuthar.wallbox.data.service.Services
import com.dilipsuthar.wallbox.utils.ThemeUtils
import com.dilipsuthar.wallbox.utils.Tools
import com.dilipsuthar.wallbox.utils.loadUrl
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.mikhaellopez.circularimageview.CircularImageView
import retrofit2.Call
import retrofit2.Response

class PhotoDetailActivity : BaseActivity() {
    private val TAG = "WallBox.PhotoDetailActivity"

    private var mService: Services? = null
    private var mOnRequestPhotoListener: Services.OnRequestPhotoListener? = null
    private var mOnRequestPhotoStatistics: Services.OnRequestPhotoStatistics? = null
    private lateinit var mPhoto: Photo
    private lateinit var mPhotoStatistics: PhotoStatistics

    @BindView(R.id.image_photo) lateinit var imgPhoto: PhotoView
    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar
    @BindView(R.id.imgUserProfile) lateinit var imgUser: CircularImageView
    @BindView(R.id.tvPhotoBy) lateinit var tvPhotoBy: TextView
    @BindView(R.id.root_view_bottom_sheet) lateinit var bottomSheet: View

    @BindView(R.id.text_description) lateinit var tvDescription: TextView
    @BindView(R.id.text_likes) lateinit var tvLikes: TextView
    @BindView(R.id.text_downloads) lateinit var tvDownload: TextView
    @BindView(R.id.text_views) lateinit var tvViews: TextView
    @BindView(R.id.text_location) lateinit var tvLocation: TextView
    @BindView(R.id.tvColorPalette) lateinit var tvColor: TextView
    @BindView(R.id.viewColor) lateinit var viewColor: CardView

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)
        ButterKnife.bind(this)

        mPhoto = Gson().fromJson(intent.getStringExtra("PHOTO"), Photo::class.java)

        /** Set mPhoto data */
        imgPhoto.setBackgroundColor(Color.parseColor(mPhoto.color))
        imgPhoto.loadUrl(mPhoto.urls.regular)
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
            else -> tvDescription.text = "no description!"
        }

        tvColor.text = mPhoto.color
        viewColor.setCardBackgroundColor(Color.parseColor(mPhoto.color))

        /** Service / API request */
        // request Photo
        mService = Services.getService()
        mOnRequestPhotoListener = object : Services.OnRequestPhotoListener {
            override fun onRequestPhotoSuccess(call: Call<Photo>, response: Response<Photo>) {
                Log.d(TAG, Gson().toJson(response.body()))
                if (response.isSuccessful) {
                    mPhoto = response.body()!!
                    setPhotoInfo()
                }
            }

            override fun onRequestPhotoFailed(call: Call<Photo>, t: Throwable) {
                Log.d(TAG, t.message!!)
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
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheet.setBackgroundColor((ContextCompat.getColor(applicationContext, R.color.cardDialogColor_night)))
                } else {
                    bottomSheet.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.overlay_dark_50))
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

    /** Methods */
    private fun initToolbar() {
        setSupportActionBar(mToolbar)
        val actionBar = supportActionBar
        actionBar?.title = ""
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.changeNavigationIconColor(mToolbar, ContextCompat.getColor(this, R.color.white))
        Tools.setSystemBarColor(this, Color.TRANSPARENT)
    }

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

    private fun setStatisticsData() {
        tvLikes.text = mPhotoStatistics.likes.total.toString()
        tvDownload.text = mPhotoStatistics.downloads.total.toString()
        tvViews.text = mPhotoStatistics.views.total.toString()
    }

    private fun setPhotoInfo() {
        if (mPhoto.location.city != "" && mPhoto.location.country != "")
            tvLocation.text = "${mPhoto.location.city}, ${mPhoto.location.country}"
        else if(mPhoto.location.city != "") tvLocation.text = mPhoto.location.city
        else if (mPhoto.location.country != "") tvLocation.text = mPhoto.location.country
        else tvLocation.text = "-"
    }

}
