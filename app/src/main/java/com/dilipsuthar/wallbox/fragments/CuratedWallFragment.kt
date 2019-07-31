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
        mSort = arguments?.getString(Preferences.SORT, "latest")

            /** SERVICES / API */
        mService = Services.getService()
        mOnRequestPhotosListener = object : Services.OnRequestPhotosListener {
            override fun onRequestPhotosSuccess(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                Log.d(WallBox.TAG, response.code().toString())
                if (mSwipeRefreshView.isRefreshing) {
                    mSwipeRefreshView.isRefreshing = false
                    Popup.showToast(context, "Updated photos", Toast.LENGTH_SHORT)
                }
                if (response.isSuccessful) {
                    loadMore = false
                    mPhotoAdapter?.removeFooter()
                    mPhotosList.clear()
                    mPhotosList.addAll(ArrayList(response.body()!!))
                    updateAdapter(mPhotosList)
                    //Tools.inVisibleViews(mProgressView as View, type = Tools.GONE)
                    Tools.visibleViews(mRecyclerView)
                } else {
                    //mPhotoAdapter?.removeFooter()
                    //Tools.inVisibleViews( mProgressView as View, type = Tools.GONE)
                    Dialog.showErrorDialog(context, Dialog.HTTP_ERROR, mPhotosList, ::load, ::loadMore)
                }
            }

            override fun onRequestPhotosFailed(call: Call<List<Photo>>, t: Throwable) {
                Log.d(WallBox.TAG, t.message)
                if (mSwipeRefreshView.isRefreshing)
                    mSwipeRefreshView.isRefreshing = false
                //mPhotoAdapter?.removeFooter()
                Dialog.showErrorDialog(context, Dialog.NETWORK_ERROR, mPhotosList, ::load, ::loadMore)
                //Tools.inVisibleViews(mProgressView as View, type = Tools.GONE)
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
                //showImagePreviewDialog(photo, imageView)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
                    HomeActivity.fabScrollUp?.hide()
                }

                val endHasBeenReached = lastVisible?.plus(2)!! >= totalItem!!   // Load more photos on last item
                if (totalItem > 0 && endHasBeenReached && !loadMore) {
                    loadMore = true
                    mPhotoAdapter?.addFooter()
                    loadMore()
                }

                verticalOffset.plus(dy)
                scrollingUp = dy > 0

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (scrollingUp) {
                        HomeActivity.fabScrollUp?.hide()
                    } else {
                        HomeActivity.fabScrollUp?.show()
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
        mSwipeRefreshView.isRefreshing = true
        mService?.requestCuratedPhotos(mPage++, WallBox.DEFAULT_PER_PAGE, mSort!!, mOnRequestPhotosListener)
        mPhotoAdapter = PhotoAdapter(ArrayList(), context!!, mOnItemClickListener)
        mRecyclerView.adapter = mPhotoAdapter
    }

    private fun loadMore() {
        mService?.requestCuratedPhotos(mPage++, WallBox.DEFAULT_PER_PAGE, mSort!!, mOnRequestPhotosListener)
    }

    private fun updateAdapter(photos: ArrayList<Photo>) {
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
