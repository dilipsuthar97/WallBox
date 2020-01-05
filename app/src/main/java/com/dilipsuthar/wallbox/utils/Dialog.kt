package com.dilipsuthar.wallbox.utils

import android.app.ProgressDialog
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Environment
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.data_source.model.Photo
import com.dilipsuthar.wallbox.helpers.DownloadHelper
import com.dilipsuthar.wallbox.threads.WallpaperApplyTask
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.IOException

object Dialog {

    fun showDownloadDialog(context: Context, photo: Photo) {
        MaterialDialog(context).show {
            title(R.string.title_select_download_quality)
            cornerRadius(16f)
            listItems(R.array.download_quality) { _, _, text ->
                var fileName: String = ""
                var url: String = ""

                when (text) {
                    "Raw" -> {
                        fileName = photo.id + " " +  text + WallBox.DOWNLOAD_PHOTO_FORMAT
                        url = photo.urls.raw
                    }
                    "Full" -> {
                        fileName = photo.id + " " +  text + WallBox.DOWNLOAD_PHOTO_FORMAT
                        url = photo.urls.full
                    }
                    "Regular" -> {
                        fileName = photo.id + " " +  text + WallBox.DOWNLOAD_PHOTO_FORMAT
                        url = photo.urls.regular
                    }
                    "Small" -> {
                        fileName = photo.id + " " +  text + WallBox.DOWNLOAD_PHOTO_FORMAT
                        url = photo.urls.small
                    }
                    "Thumb" -> {
                        fileName = photo.id + " " +  text + WallBox.DOWNLOAD_PHOTO_FORMAT
                        url = photo.urls.thumb
                    }
                }

                dismiss()
                if (DownloadHelper.getInstance(context)?.fileExists(fileName)!!) {
                    PopupUtils.showToast(context, "$fileName ${context.resources.getString(R.string.msg_photo_downloaded)}", Toast.LENGTH_SHORT)
                } else {
                    PopupUtils.showToast(context, context.resources.getString(R.string.msg_download_start), Toast.LENGTH_SHORT)
                    DownloadHelper.getInstance(context)?.addDownloadRequest(url, fileName)
                }
            }
        }
    }

    fun showSetWallpaperDialog(context: Context, view: View, photo: Photo) {
        MaterialDialog(context).show {
            title(R.string.title_select_quality)
            cornerRadius(16f)
            listItems(R.array.download_quality) { _, _, text ->
                var fileName: String = ""
                var url: String = ""

                when (text) {
                    "Raw" -> {
                        fileName = photo.id + " " +  text + WallBox.DOWNLOAD_PHOTO_FORMAT
                        url = photo.urls.raw
                    }
                    "Full" -> {
                        fileName = photo.id + " " +  text + WallBox.DOWNLOAD_PHOTO_FORMAT
                        url = photo.urls.full
                    }
                    "Regular" -> {
                        fileName = photo.id + " " +  text + WallBox.DOWNLOAD_PHOTO_FORMAT
                        url = photo.urls.regular
                    }
                    "Small" -> {
                        fileName = photo.id + " " +  text + WallBox.DOWNLOAD_PHOTO_FORMAT
                        url = photo.urls.small
                    }
                    "Thumb" -> {
                        fileName = photo.id + " " +  text + WallBox.DOWNLOAD_PHOTO_FORMAT
                        url = photo.urls.thumb
                    }
                }

                dismiss()
                WallpaperApplyTask(fileName, url, photo, context, view).execute()

            }
        }
    }

}