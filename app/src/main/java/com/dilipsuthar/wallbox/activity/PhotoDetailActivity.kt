package com.dilipsuthar.wallbox.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.data.model.Photo
import com.google.gson.Gson
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class PhotoDetailActivity : BaseActivity() {

    @BindView(R.id.image_photo) lateinit var imagePhoto: ImageView

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

        Picasso.get()
            .load(photo.urls.regular)
            .placeholder(R.drawable.gradient_overlay_dark)
            .error(R.drawable.gradient_overlay_dark)
            .into(imagePhoto)

    }

}
