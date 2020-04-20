package com.dilipsuthar.wallbox.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.helpers.setRefresh
import com.dilipsuthar.wallbox.preferences.Prefs
/**
 * Created by DILIP SUTHAR on 28/07/19
 */
class CuratedWallFragment : BasePhotosFragment() {

    companion object {
        const val TAG = "Fragment.CuratedWall"
        fun newInstance(sort: String): CuratedWallFragment {
            val fragment = CuratedWallFragment()

            val args = Bundle()
            args.putString(Prefs.SORT, sort)
            fragment.arguments = args

            return fragment
        }
    }

    override fun getView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_curated_wall, container, false)
    }

    override fun loadPhotos(totalItem: Int) {
        mSwipeRefreshLayout setRefresh true
        loadMore = true
        if (snackBar != null) snackBar?.dismiss()
        mService?.requestCuratedPhotos(mPage, WallBox.DEFAULT_PER_PAGE, mSort!!, this)
    }
}
