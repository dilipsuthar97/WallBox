package com.dilipsuthar.wallbox.data.repository

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.data.model.Photo
import com.dilipsuthar.wallbox.data.service.Services
import retrofit2.Call
import retrofit2.Response

class WallpaperRepository(val context: Context?) {

    // VARS
    private var PAGE: Int = 0
    private var mServices: Services? = null
    private var mOnRequestPhotoListener: Services.OnRequestPhotosListener? = null

    private var mWallpapers: MutableLiveData<List<Photo>> = MutableLiveData()

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: WallpaperRepository? = null

        fun getInstance(context: Context?): WallpaperRepository {
            if (instance == null)
                instance = WallpaperRepository(context)

            return instance as WallpaperRepository
        }
    }

    init {

        mServices = Services.getService()

        mServices?.requestPhotos(PAGE++, WallBox.DEFAULT_PER_PAGE, "latest", mOnRequestPhotoListener)
    }

    public fun getWallpapers(): LiveData<List<Photo>> {
        // TODO: Call listener methods here ^^
        mOnRequestPhotoListener = object : Services.OnRequestPhotosListener {
            override fun onRequestPhotosSuccess(call: Call<List<Photo>>, response: Response<List<Photo>>) {

            }

            override fun onRequestPhotosFailed(call: Call<List<Photo>>, t: Throwable) {

            }
        }

        mServices?.requestPhotos(PAGE++, WallBox.DEFAULT_PER_PAGE, WallBox.DEFAULT_ORDER_BY, mOnRequestPhotoListener)

        return mWallpapers
    }

}