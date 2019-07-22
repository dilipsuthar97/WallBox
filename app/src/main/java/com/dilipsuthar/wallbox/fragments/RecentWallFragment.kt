package com.dilipsuthar.wallbox.fragments


import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.airbnb.lottie.LottieAnimationView

import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.adapters.PhotoAdapter
import com.dilipsuthar.wallbox.data.model.Photo
import com.dilipsuthar.wallbox.data.service.PhotoService
import com.dilipsuthar.wallbox.preferences.Preferences
import com.dilipsuthar.wallbox.utils.Tools
import com.dilipsuthar.wallbox.utils.VerticalSpacingItemDecorator
import com.dilipsuthar.wallbox.viewmodels.WallpaperListViewModel
import retrofit2.Call
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 *
 */
class RecentWallFragment : Fragment() {

    // VARS
    private var mWallpaperListViewModel: WallpaperListViewModel? = null

    private var mService: PhotoService? = null
    private var mOnRequestPhotosListener: PhotoService.OnRequestPhotosListener? = null
    private var mPage: Int = 0
    private var mSort: String? = null
    private var mPhotosList: ArrayList<Photo> = ArrayList()
    private var mPhotoAdapter: PhotoAdapter? = null


    // VIEWS
    @BindView(R.id.recent_wallpaper_list) lateinit var mRecyclerView: RecyclerView
    @BindView(R.id.progress) lateinit var mProgressView: LottieAnimationView
    @BindView(R.id.network_error_layout) lateinit var mNetworkErrorView: View
    @BindView(R.id.http_error_layout) lateinit var mHttpErrorView: View
    @BindView(R.id.recent_swipe_refresh_layout) lateinit var mSwipRefreshView: SwipeRefreshLayout

    // MAIN FUNCTIONS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WallBox.getInstance())
        mSort = sharedPreferences.getString(Preferences.SORT, "latest")

        mService = PhotoService.getService()

        mOnRequestPhotosListener = object : PhotoService.OnRequestPhotosListener {
            override fun onRequestPhotosSuccess(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                Log.d(WallBox.TAG, response.code().toString())
                if (mSwipRefreshView.isRefreshing) {
                    mSwipRefreshView.isRefreshing = false
                    Toast.makeText(context, "Updated photos", Toast.LENGTH_SHORT).show()
                }
                if (response.isSuccessful) {
                    mPhotosList.addAll(ArrayList(response.body()))
                    updateAdapter(mPhotosList)
                    mRecyclerView.adapter = mPhotoAdapter
                    Tools.inVisibleViews(mProgressView, mNetworkErrorView, mHttpErrorView, type = 1)
                    Tools.visibleViews(mRecyclerView)
                } else {
                    Tools.inVisibleViews(mRecyclerView, mNetworkErrorView, mProgressView, type = 1)
                    Tools.visibleViews(mHttpErrorView)
                }
            }

            override fun onRequestPhotosFailed(call: Call<List<Photo>>, t: Throwable) {
                Log.d(WallBox.TAG, t.message)
                mSwipRefreshView.isRefreshing = false
                Tools.visibleViews(mNetworkErrorView)
                Tools.inVisibleViews(mRecyclerView, mHttpErrorView, mProgressView, type = 1)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recent_wall, container, false)
        ButterKnife.bind(this, view)

        initRecyclerView()

        //mSwipRefreshView.isRefreshing = true
        mSwipRefreshView.setOnRefreshListener {
            mPage = 0
            mPhotosList.clear()
            load()
        }

        mPage = 0
        mService?.requestPhotos(mPage++, WallBox.DEFAULT_PER_PAGE, WallBox.DEDAULT_ORDER_BY, mOnRequestPhotosListener)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mService != null)
            mService?.cancel()
    }

    private fun load() {
        /*mProgressView.visibility =View.VISIBLE
        mRecyclerView.visibility = View.GONE
        mNetworkErrorView.visibility = View.GONE
        mHttpErrorView.visibility = View.GONE*/
        Tools.visibleViews(mProgressView)
        Tools.inVisibleViews(mRecyclerView, mNetworkErrorView, mHttpErrorView, type = 1)
        mService?.requestPhotos(mPage++, WallBox.DEFAULT_PER_PAGE, WallBox.DEDAULT_ORDER_BY, mOnRequestPhotosListener)

    }

    private fun loadMore() {
        mService?.requestPhotos(mPage++, WallBox.DEFAULT_PER_PAGE, WallBox.DEDAULT_ORDER_BY, mOnRequestPhotosListener)
    }

    private fun updateAdapter(photos: ArrayList<Photo>) {
        mPhotoAdapter = PhotoAdapter(photos, context!!)
        mPhotoAdapter?.notifyDataSetChanged()
    }

    private fun subscribeObservers() {
        mWallpaperListViewModel = ViewModelProviders.of(activity!!).get(WallpaperListViewModel::class.java)

    }

    private fun initRecyclerView() {
        //val itemDecorator = VerticalSpacingItemDecorator(5)
        //mRecyclerView.addItemDecoration(itemDecorator)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setItemViewCacheSize(5)
        /*mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(0)) {
                    loadMore()
                }
            }
        })*/
    }

}
