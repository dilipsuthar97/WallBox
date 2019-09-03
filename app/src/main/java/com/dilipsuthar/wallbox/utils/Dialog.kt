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
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.IOException

object
Dialog {

    fun showDownloadDialog(context: Context, photo: Photo) {
        MaterialDialog(context).show {
            title(text = "Select Download Quality")
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

                if (DownloadHelper.getInstance(context)?.fileExists(fileName)!!) {
                    PopupUtils.showToast(context, "File $fileName is already downloaded", Toast.LENGTH_SHORT)
                } else {
                    PopupUtils.showToast(context, "Downloading started...", Toast.LENGTH_SHORT)
                    DownloadHelper.getInstance(context)?.addDownloadRequest(url, fileName)
                }
            }
        }
    }

    fun showSetWallpaperDialog(context: Context, view: View, photo: Photo) {
        MaterialDialog(context).show {
            title(text = "Select Quality")
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

                SetWallpaperTask(fileName, url, photo, context, view).execute()

            }
        }
    }

    private class SetWallpaperTask(
        private val fileName: String,
        private val url: String,
        private val photo: Photo,
        private val context: Context,
        private val view: View) : AsyncTask<Void, Void, Boolean>() {

        private var pDialog: ProgressDialog? = null

        override fun onPreExecute() {
            super.onPreExecute()
            pDialog = ProgressDialog(context)
            with(pDialog!!) {
                setMessage("Please wait...")
                setProgressStyle(ProgressDialog.STYLE_SPINNER)
                setCancelable(false)
                show()
            }
        }

        override fun doInBackground(vararg p0: Void?): Boolean? {
            var bitmap: Bitmap? = null

            if (DownloadHelper.getInstance(context)?.fileExists(fileName)!!) {

                /*val uri = FileProvider.getUriForFile(
                    context,
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    File(Environment.getExternalStorageDirectory().absolutePath + WallBox.DOWNLOAD_PATH + fileName)
                )*/
                return try {
                    val file = File(Environment.getExternalStorageDirectory().absolutePath + WallBox.DOWNLOAD_PATH + fileName)
                    bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    val manager = WallpaperManager.getInstance(context)
                    manager.setBitmap(bitmap)
                    pDialog?.dismiss()

                    true
                } catch (e: IOException) {
                    e.printStackTrace()

                    false
                }

            } else {

                try {

                    //bitmap = Picasso.get().load(url).get()
                    Glide.with(context)
                        .asBitmap()
                        .load(url)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                bitmap = resource
                                val manager = WallpaperManager.getInstance(context)
                                manager.setBitmap(bitmap)
                                pDialog?.dismiss()
                                PopupUtils.showToast(context, "Wallpaper set successfully", Toast.LENGTH_SHORT)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {

                            }

                            override fun onLoadFailed(errorDrawable: Drawable?) {
                                pDialog?.dismiss()
                                Snackbar.make(view, "Failed to load wallpaper", Snackbar.LENGTH_SHORT)
                                    .setAction("DOWNLOAD") {
                                        showDownloadDialog(context, photo)
                                    }
                                    .show()
                            }
                        })

                    return true

                } catch (e: IOException) {
                    e.printStackTrace()
                    return false
                }

            }
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            /*if (result == true) PopupUtils.showToast(context, "Wallpaper set successfully", Toast.LENGTH_SHORT)
            else PopupUtils.showToast(context, "Wallpaper not set", Toast.LENGTH_SHORT)
            pDialog?.dismiss()*/
        }
    }

}