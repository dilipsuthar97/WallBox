package com.dilipsuthar.wallbox.threads

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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.data_source.model.Photo
import com.dilipsuthar.wallbox.helpers.DownloadHelper
import com.dilipsuthar.wallbox.utils.Dialog
import com.dilipsuthar.wallbox.utils.PopupUtils
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.IOException

class WallpaperApplyTask(
    private val fileName: String,
    private val url: String,
    private val photo: Photo,
    private val context: Context,
    private val view: View
) : AsyncTask<Void, Void, Boolean>() {

    private var pDialog: ProgressDialog? = null

    override fun onPreExecute() {
        super.onPreExecute()
        pDialog = ProgressDialog(context)
        with(pDialog!!) {
            setMessage(context.resources.getString(R.string.msg_please_wait))
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
                PopupUtils.showToast(context, context.resources.getString(R.string.msg_wallpaper_set_success), Toast.LENGTH_SHORT)

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
                            PopupUtils.showToast(context, context.resources.getString(R.string.msg_wallpaper_set_success), Toast.LENGTH_SHORT)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            pDialog?.dismiss()
                            Snackbar.make(view, context.resources.getString(R.string.msg_wallpaper_set_failed), Snackbar.LENGTH_SHORT)
                                .setAction("DOWNLOAD") {
                                    Dialog.showDownloadDialog(context, photo)
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