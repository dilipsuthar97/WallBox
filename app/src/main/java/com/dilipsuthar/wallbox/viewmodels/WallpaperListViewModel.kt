package com.dilipsuthar.wallbox.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dilipsuthar.wallbox.data_source.model.Photo
import com.dilipsuthar.wallbox.data_source.repository.WallpaperRepository

class WallpaperListViewModel(context: Context?) : ViewModel() {

    private var wallpaperRepository: WallpaperRepository? = null

    init {
        wallpaperRepository = WallpaperRepository.newInstance()
    }

    fun getWallpapers() : LiveData<List<Photo>>? {
        return wallpaperRepository?.getWallpapers()
    }

}