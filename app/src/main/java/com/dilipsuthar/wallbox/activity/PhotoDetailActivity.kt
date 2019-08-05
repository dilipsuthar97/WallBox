package com.dilipsuthar.wallbox.activity

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.data.model.Photo
import com.dilipsuthar.wallbox.utils.ThemeUtils
import com.dilipsuthar.wallbox.utils.Tools
import com.github.chrisbanes.photoview.PhotoView
import com.google.gson.Gson
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class PhotoDetailActivity : BaseActivity() {

    @BindView(R.id.image_photo) lateinit var imagePhoto: PhotoView
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)
        ButterKnife.bind(this)

        val photo: Photo = Gson().fromJson(intent.getStringExtra("PHOTO"), Photo::class.java)

        /*Glide.with(this)
            .load(photo.urls.regular)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.gradient_overlay_dark)
            .error(R.drawable.gradient_overlay_dark)
            .transition(DrawableTransitionOptions().crossFade())
            .into(imagePhoto)*/

        imagePhoto.setBackgroundColor(Color.parseColor(photo.color))
        Picasso.get()
            .load(photo.urls.regular)
            .placeholder(R.drawable.gradient_overlay_dark)
            .error(R.drawable.gradient_overlay_dark)
            .into(imagePhoto)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

       initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title = ""
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.changeNavigationIconColor(toolbar, resources.getColor(R.color.white))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
