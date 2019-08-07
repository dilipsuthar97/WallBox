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

class WallpaperListViewModel(context: Context?) : ViewModel() {

    private var wallpaperRepository: WallpaperRepository? = null

    init {
        wallpaperRepository = WallpaperRepository.newInstance()
    }

    fun getWallpapers() : LiveData<List<Photo>>? {
        return wallpaperRepository?.getWallpapers()
    }

}