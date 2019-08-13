package com.dilipsuthar.wallbox.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.data.model.Photo
import com.dilipsuthar.wallbox.data.service.Services
import com.dilipsuthar.wallbox.utils.Tools
import com.dilipsuthar.wallbox.utils.loadUrl
import com.github.chrisbanes.photoview.PhotoView
import com.google.gson.Gson
import com.mikhaellopez.circularimageview.CircularImageView
import retrofit2.Call
import retrofit2.Response

class PhotoFullActivity : AppCompatActivity() {

    private val TAG = "WallBox.PhotoDetailActivity"

    private var mService: Services? = null
    private var mOnRequestPhotoListener: Services.OnRequestPhotoListener? = null

    @BindView(R.id.image_photo) lateinit var mImagePhoto: PhotoView
    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar
    @BindView(R.id.image_user_profile) lateinit var mImageUser: CircularImageView
    @BindView(R.id.text_photo_by) lateinit var mTextPhotoBy: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_full)

        ButterKnife.bind(this)

        var photo: Photo = Gson().fromJson(intent.getStringExtra("PHOTO"), Photo::class.java)

        mImagePhoto.setBackgroundColor(Color.parseColor("#000000"))
        mImagePhoto.loadUrl(photo.urls.regular)
        mImageUser.loadUrl(
            photo.user.profile_image.large,
            R.drawable.placeholder_profile,
            R.drawable.placeholder_profile)

        val txtPhotoBy = photo.user.first_name
        if (photo.user.last_name != null) txtPhotoBy + " ${photo.user.last_name}"
        mTextPhotoBy.text = txtPhotoBy

        /** Service / API request */
        mService = Services.getService()
        mOnRequestPhotoListener = object : Services.OnRequestPhotoListener {
            override fun onRequestPhotoSuccess(call: Call<Photo>, response: Response<Photo>) {
                Log.d(TAG, Gson().toJson(response.body()))
                if (response.isSuccessful) photo = response.body()!!
            }

            override fun onRequestPhotoFailed(call: Call<Photo>, t: Throwable) {
                Log.d(TAG, t.message!!)
            }
        }

        //mService?.requestPhoto(photo.id, mOnRequestPhotoListener)

        // Make statusBar & NavigationBar transparent
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)

        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(mToolbar)
        val actionBar = supportActionBar
        actionBar?.title = ""
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.changeNavigationIconColor(mToolbar, ContextCompat.getColor(this, R.color.white))
        Tools.setSystemBarColor(this, Color.TRANSPARENT)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_photo_detail, menu)
        Tools.changeMenuIconColor(menu!!, ContextCompat.getColor(this, R.color.white))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.scale_center_crop -> mImagePhoto.scaleType = ImageView.ScaleType.CENTER_CROP
            R.id.scale_center -> mImagePhoto.scaleType = ImageView.ScaleType.CENTER
            R.id.scale_fit_ceter -> mImagePhoto.scaleType = ImageView.ScaleType.FIT_CENTER
            R.id.scale_fit_xy -> mImagePhoto.scaleType = ImageView.ScaleType.FIT_XY
        }
        return super.onOptionsItemSelected(item)
    }
}
