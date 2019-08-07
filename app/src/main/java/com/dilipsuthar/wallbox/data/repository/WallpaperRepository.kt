package com.dilipsuthar.wallbox.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dilipsuthar.wallbox.data.model.Photo
import com.dilipsuthar.wallbox.data.service.Services

class WallpaperRepository {

    private var mWallpapers: MutableLiveData<List<Photo>>? = MutableLiveData()

    companion object {
        private var instance: WallpaperRepository? = null
        fun newInstance(): WallpaperRepository? {
            if (instance == null) instance = WallpaperRepository()
            return instance
        }
    }

    init {

    }

    fun getWallpapers(): LiveData<List<Photo>>? {
        return mWallpapers
    }

}