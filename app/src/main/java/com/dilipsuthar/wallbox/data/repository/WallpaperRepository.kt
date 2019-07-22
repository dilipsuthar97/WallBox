package com.dilipsuthar.wallbox.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.data.model.Photo
import com.dilipsuthar.wallbox.data.service.PhotoService
import retrofit2.Call
import retrofit2.Response

class WallpaperRepository {

    // VARS
    private var PAGE: Int = 0
    private var mPhotoService: PhotoService? = null
    private var mOnRequestPhotoListener: PhotoService.OnRequestPhotosListener? = null

    private var mWallpapers: MutableLiveData<List<Photo>> = MutableLiveData()

    companion object {
        private var instance: WallpaperRepository? = null

        fun getInstance(): WallpaperRepository {
            if (instance == null)
                instance = WallpaperRepository()

            return instance as WallpaperRepository
        }
    }

    init {

        mPhotoService = PhotoService.getService()

        mPhotoService?.requestPhotos(PAGE++, WallBox.DEFAULT_PER_PAGE, "latest", mOnRequestPhotoListener)
    }

    public fun getWallpapers(): LiveData<List<Photo>> {
        // TODO: Call listener methods here ^^
        mOnRequestPhotoListener = object : PhotoService.OnRequestPhotosListener {
            override fun onRequestPhotosSuccess(call: Call<List<Photo>>, response: Response<List<Photo>>) {

            }

            override fun onRequestPhotosFailed(call: Call<List<Photo>>, t: Throwable) {

            }
        }

        mPhotoService?.requestPhotos(PAGE++, WallBox.DEFAULT_PER_PAGE, WallBox.DEDAULT_ORDER_BY, mOnRequestPhotoListener)

        return mWallpapers
    }

}