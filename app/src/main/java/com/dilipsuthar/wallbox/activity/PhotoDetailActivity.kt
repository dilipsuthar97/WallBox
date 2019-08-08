package com.dilipsuthar.wallbox.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
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
import retrofit2.Call
import retrofit2.Response

class PhotoDetailActivity : BaseActivity() {
    private val TAG = "WallBox.PhotoDetailActivity"

    private var mService: Services? = null
    private var mOnRequestPhotoListener: Services.OnRequestPhotoListener? = null

    @BindView(R.id.image_photo) lateinit var mImagePhoto: PhotoView
    @BindView(R.id.toolbar) lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)
        ButterKnife.bind(this)

        var photo: Photo = Gson().fromJson(intent.getStringExtra("PHOTO"), Photo::class.java)

        mImagePhoto.setBackgroundColor(Color.parseColor("#000000"))
        mImagePhoto.loadUrl(photo.urls.regular)

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

        mService?.requestPhoto(photo.id, mOnRequestPhotoListener)

       /* window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)*/

       /* window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide status bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN)*/

        initToolbar()
    }

    private fun initToolbar() {
        /*val rectangle = Rect()
        window.decorView.getWindowVisibleDisplayFrame(rectangle)
        val params = mToolbar.layoutParams as Toolbar.LayoutParams
        params.setMargins(0, 24, 0, 0)
        mToolbar.layoutParams = params*/
        setSupportActionBar(mToolbar)
        val actionBar = supportActionBar
        actionBar?.title = ""
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.changeNavigationIconColor(mToolbar, ContextCompat.getColor(this, R.color.white))
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
