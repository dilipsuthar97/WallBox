package com.dilipsuthar.wallbox.viewholders

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.adapters.PhotoAdapter
import com.dilipsuthar.wallbox.data_source.model.Photo
import com.dilipsuthar.wallbox.helpers.PermissionsHelper
import com.dilipsuthar.wallbox.helpers.loadUrl
import com.dilipsuthar.wallbox.preferences.Prefs
import com.dilipsuthar.wallbox.preferences.SharedPref
import com.dilipsuthar.wallbox.utils.Dialog
import com.mikhaellopez.circularimageview.CircularImageView

class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    init {
        ButterKnife.bind(this, itemView)
    }

    @BindView(R.id.image_photo) lateinit var imagePhoto: ImageView
    @BindView(R.id.img_photo_by) lateinit var imgPhotoBy: CircularImageView
    @BindView(R.id.text_photo_by) lateinit var textPhotoBy: TextView
    @BindView(R.id.text_likes) lateinit var textLikes: TextView
    @BindView(R.id.root_view) lateinit var rootView: View
    @BindView(R.id.btn_download) lateinit var btnDownload: ImageButton
    @BindView(R.id.lyt_photo_by) lateinit var lytPhotoBy: View

    fun bind(
        photo: Photo?,
        position: Int,
        ctx: Context?,
        act: Activity?,
        listener: PhotoAdapter.OnItemClickListener?
    ) {
        photo?.let {
            val txtPhotoBy = photo.user.first_name
            if (it.user.last_name != "") txtPhotoBy + " ${it.user.last_name}"

            rootView.setBackgroundColor(Color.parseColor(it.color))
            textPhotoBy.text = "By $txtPhotoBy"
            textLikes.text = "${it.likes} Likes"
            btnDownload.setOnClickListener { _ ->
                if (PermissionsHelper.permissionGranted(ctx!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {

                    Dialog.showDownloadDialog(ctx, it)

                } else
                    PermissionsHelper.requestPermission(
                        act!!,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    )
            }

            val url = when (SharedPref.getInstance(ctx!!).getString(Prefs.WALLPAPER_QUALITY, WallBox.DEFAULT_WALLPAPER_QUALITY)) {
                "Raw" -> it.urls.raw
                "Full" -> it.urls.full
                "Regular" -> it.urls.regular
                "Small" -> it.urls.small
                else -> it.urls.thumb
            }
            imagePhoto.loadUrl(url)

            imgPhotoBy.loadUrl(
                it.user.profile_image.large,
                ctx.getDrawable(R.drawable.placeholder_profile),
                ctx.getDrawable(R.drawable.placeholder_profile))

            // onClick listener
            imagePhoto.setOnLongClickListener { view ->
                listener?.onItemLongClick(it, view!!, position, imagePhoto)
                true
            }

            imagePhoto.setOnClickListener { view ->
                listener?.onItemClick(it, view, position, imagePhoto)
            }

            lytPhotoBy.setOnClickListener { _ ->
                listener?.onUserProfileClick(it, position, imgPhotoBy)
            }
        }
    }
}