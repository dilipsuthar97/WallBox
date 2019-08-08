package com.dilipsuthar.wallbox.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife

import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.activity.HomeActivity
import com.dilipsuthar.wallbox.activity.PhotoDetailActivity
import com.dilipsuthar.wallbox.adapters.PhotoAdapter
import com.dilipsuthar.wallbox.data.model.Photo
import com.dilipsuthar.wallbox.data.service.Services
import com.dilipsuthar.wallbox.preferences.Preferences
import com.dilipsuthar.wallbox.utils.Dialog
import com.dilipsuthar.wallbox.utils.Popup
import com.dilipsuthar.wallbox.utils.Tools
import com.dilipsuthar.wallbox.utils.setRefresh
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response

/**
 * Created by Dilip on 28/07/19
 */

class CuratedWallFragment : Fragment() {

    companion object {
        const val TAG = "WallBox.CuratedWallFragment"
        fun newInstance(sort: String): CuratedWallFragment {
            val fragment = CuratedWallFragment()

            val args = Bundle()
            args.putString(Preferences.SORT, sort)
            fragment.arguments = args

            return fragment
        }
    }

    // VIEWS
    private var mService: Services? = null
    private var mOnRequestPhotosListener: Services.OnRequestPhotosListener? = null
    private var mPage: Int = 0
    private var mSort: String? = null
    private var mPhotosList: ArrayList<Photo> = ArrayList()
    private var mPhotoAdapter: PhotoAdapter? = null
    private var mOnItemClickListener: PhotoAdapter.OnItemClickListener? = null
    private var loadMore: Boolean = false

    // VIEWS
    @BindView(R.id.curated_wallpaper_list) lateinit var mRecyclerView: RecyclerView
    @BindView(R.id.curated_swipe_refresh_layout) lateinit var mSwipeRefreshView: SwipeRefreshLayout

    /** MAIN METHOD */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSort = arguments?.getString(Preferences.SORT, WallBox.DEFAULT_SORT_PHOTOS)

            /** SERVICES / API */
        mService = Services.getService()
        mOnRequestPhotosListener = object : Services.OnRequestPhotosListener {
            override fun onRequestPhotosSuccess(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                Log.d(TAG, response.code().toString())
                mSwipeRefreshView setRefresh false
                if (!loadMore)  Popup.showToast(context, "Updated photos", Toast.LENGTH_SHORT)
                if (response.isSuccessful) {
                    loadMore = false
                    mPhotosList.clear()
                    mPhotosList.addAll(ArrayList(response.body()!!))
                    updateAdapter(mPhotosList)
                    Tools.visibleViews(mRecyclerView)
                } else {
                    Dialog.showErrorDialog(context, Dialog.HTTP_ERROR, mPhotosList, ::load, ::loadMore)
                }
            }

            override fun onRequestPhotosFailed(call: Call<List<Photo>>, t: Throwable) {
                Log.d(WallBox.TAG, t.message)
                mSwipeRefreshView setRefresh false
                Dialog.showErrorDialog(context, Dialog.NETWORK_ERROR, mPhotosList, ::load, ::loadMore)
            }
        }

        /** ADAPTER LISTENERS */
        mOnItemClickListener = object : PhotoAdapter.OnItemClickListener {
            override fun onItemClick(photo: Photo, view: View, pos: Int, imageView: ImageView) {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, imageView, ViewCompat.getTransitionName(imageView)!!)
                val intent = Intent(activity, PhotoDetailActivity::class.java)
                intent.putExtra("PHOTO", Gson().toJson(photo))
                startActivity(intent, options.toBundle())
            }

            override fun onItemLongClick(photo: Photo, view: View, pos: Int, imageView: ImageView) {
                Popup.showToast(context, "$pos", Toast.LENGTH_SHORT)
                Log.d(WallBox.TAG, "mOnItemClickListener: onItemLongClick")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView: called >>>>>")

        retainInstance = true
        val view = inflater.inflate(R.layout.fragment_curated_wall, container, false)
        ButterKnife.bind(this, view)

        initComponent()

        mPage = 1
        load()

        return view
    }

    /** Methods */
    private fun initComponent() {
        /** Recycler View */
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.setItemViewCacheSize(5)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            var verticalOffset: Int = 0
            var scrollingUp: Boolean = false

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                /** check for first & last item position */
                val layoutManager = LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
                val totalItem = layoutManager?.itemCount
                val lastVisible = layoutManager?.findLastVisibleItemPosition()
                val firstVisible = layoutManager?.findFirstCompletelyVisibleItemPosition()

                val atTopReached = firstVisible?.minus(1)!! <= 0    // Hide fab on first item
                if (atTopReached) {
                    // TODO: hide fabScrollUp here
                }

                val endHasBeenReached = lastVisible?.plus(2)!! >= totalItem!!   // Load more photos on last item
                if (totalItem > 0 && endHasBeenReached && !loadMore) {
                    loadMore = true
                    mSwipeRefreshView setRefresh true
                    loadMore()
                }

                verticalOffset.plus(dy)
                scrollingUp = dy > 0

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (scrollingUp) {
                        // TODO: hide fabScrollUp here
                    } else {
                        // TODO: show fabScrollUp here
                    }
                }
            }

        })


        /** Swipe Refresh Layout */
        mSwipeRefreshView.setOnRefreshListener {
            mPage = 1
            mPhotosList.clear()
            load()
        }

    }

    private fun load() {
        Log.d(TAG, "load: called >>>>>>>>>>")

        mSwipeRefreshView setRefresh true
        mService?.requestCuratedPhotos(mPage++, WallBox.DEFAULT_PER_PAGE, mSort!!, mOnRequestPhotosListener)
        mPhotoAdapter = PhotoAdapter(ArrayList(), context!!, mOnItemClickListener)
        mRecyclerView.adapter = mPhotoAdapter
    }

    private fun loadMore() {
        Log.d(TAG, "loadMore: called >>>>>>>>>>")

        mService?.requestCuratedPhotos(mPage++, WallBox.DEFAULT_PER_PAGE, mSort!!, mOnRequestPhotosListener)
    }

    private fun updateAdapter(photos: ArrayList<Photo>) {
        Log.d(TAG, "updateAdapter: called >>>>>>>>>>")

        mPhotoAdapter?.addAll(photos)
    }

    fun scrollToTop() {
        val layoutManager = mRecyclerView.layoutManager as LinearLayoutManager
        if (mRecyclerView != null) {
            if (layoutManager != null && layoutManager.findFirstVisibleItemPosition() > 5) {
                mRecyclerView.scrollToPosition(5)
            }
            mRecyclerView.smoothScrollToPosition(0)
        }

    }

}
