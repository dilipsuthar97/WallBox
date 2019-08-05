package com.dilipsuthar.wallbox.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.data.model.Photo
import com.dilipsuthar.wallbox.data.repository.WallpaperRepository

class WallpaperListViewModel(application: Application, context: Context?) : AndroidViewModel(application) {

    private var wallpaperRepository: WallpaperRepository? = null
    private var mWallpapers: MutableLiveData<List<Photo>> = MutableLiveData()

    init {
        wallpaperRepository = WallpaperRepository.getInstance(context)
    }

    public fun getWallpapers() : LiveData<List<Photo>> {
        return mWallpapers
    }

}