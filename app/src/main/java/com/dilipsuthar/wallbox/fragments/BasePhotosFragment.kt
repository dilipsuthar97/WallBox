package com.dilipsuthar.wallbox.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.activity.PhotoDetailActivity
import com.dilipsuthar.wallbox.activity.ProfileActivity
import com.dilipsuthar.wallbox.adapters.PhotoAdapter
import com.dilipsuthar.wallbox.data_source.model.Photo
import com.dilipsuthar.wallbox.data_source.Services
import com.dilipsuthar.wallbox.helpers.setRefresh
import com.dilipsuthar.wallbox.preferences.Preferences
import com.dilipsuthar.wallbox.utils.PopupUtils
import com.dilipsuthar.wallbox.utils.Tools
import com.dilipsuthar.wallbox.utils.itemDecorater.HorizontalSpacingItemDecorator
import com.dilipsuthar.wallbox.utils.itemDecorater.VerticalSpacingItemDecorator
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.mikhaellopez.circularimageview.CircularImageView
import retrofit2.Call
import retrofit2.Response

abstract class BasePhotosFragment : Fragment(), Services.OnRequestPhotosListener, PhotoAdapter.OnItemClickListener {
    private val TAG = BasePhotosFragment::class.java.simpleName

    var mService: Services? = null
    //lateinit var mOnRequestPhotosListener: Services.OnRequestPhotosListener
    var mPage = 0
    var mSort: String? = null
    lateinit var mPhotoAdapter: PhotoAdapter
    //var mOnItemClickListener: PhotoAdapter.OnItemClickListener? = null
    var mPhotosList: ArrayList<Photo> = ArrayList()
    var loadMore: Boolean = false
    var snackBar: Snackbar? = null

    @BindView(R.id.recycler_view) lateinit var mRecyclerView: RecyclerView
    @BindView(R.id.swipe_refresh_layout) lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    @BindView(R.id.network_error_layout) lateinit var lytNetworkError: LinearLayout
    @BindView(R.id.http_error_layout) lateinit var lytHttpError: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)

        mSort = arguments?.getString(Preferences.SORT, WallBox.DEFAULT_SORT_PHOTOS)

        /** SERVICES / API */
        mService = Services.getService()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true
        val view = getView(inflater, container, savedInstanceState)
        ButterKnife.bind(this, view)

        /** Recycler View */
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.addItemDecoration(VerticalSpacingItemDecorator(22))
        mRecyclerView.setItemViewCacheSize(5)
        mPhotoAdapter = PhotoAdapter(ArrayList(), "list", context, activity, this)
        mRecyclerView.adapter = mPhotoAdapter

        mPage = 1
        loadPhotos(mRecyclerView.layoutManager?.itemCount!!)

        /** Views listeners */
        // RecyclerView listener
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            var verticalOffset: Int = 0
            var scrollingUp: Boolean = false

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                /** check for first & last item position */
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItem = layoutManager.itemCount
                val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()
                val firstVisible = layoutManager.findFirstCompletelyVisibleItemPosition()

                val atTopReached = firstVisible.minus(1) <= 0    // Hide fab on first item
                if (atTopReached) run {
                    // TODO: hide fabScrollUp here
                }

                val endHasBeenReached = lastVisible.plus(1) >= totalItem   // Load more photos on last item
                if (totalItem > 0 && endHasBeenReached && !loadMore) {
                    //loadMore = true
                    loadPhotos(totalItem)
                }

                verticalOffset += dy
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

        // Swipe refresh listener
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(context!!, R.color.colorAccent))
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.white))
        mSwipeRefreshLayout.setOnRefreshListener {
            mPage = 1
            mPhotosList.clear()
            loadPhotos(mRecyclerView.layoutManager?.itemCount!!)
            mPhotoAdapter = PhotoAdapter(ArrayList(), "list", context, activity, this)
            mRecyclerView.adapter = mPhotoAdapter
        }

        /** Event listener */
        lytNetworkError.setOnClickListener {
            loadPhotos(mRecyclerView.layoutManager?.itemCount!!)
            it.visibility = View.GONE
        }
        lytHttpError.setOnClickListener {
            loadPhotos(mRecyclerView.layoutManager?.itemCount!!)
            it.visibility = View.GONE
        }

        return view
    }

    /** Methods */
    /*fun load() {
        mSwipeRefreshLayout setRefresh true
        loadMore = true
        if (snackBar != null) snackBar?.dismiss()
        loadPhotos()
        //mService?.requestPhotos(mPage, WallBox.DEFAULT_PER_PAGE, mSort!!, mOnRequestPhotosListener)
    }*/

    private fun updateAdapter(photos: ArrayList<Photo>) {
        mPhotoAdapter.addAll(photos)
    }

    /*fun scrollToTop() {
        val layoutManager = mRecyclerView.layoutManager as LinearLayoutManager
        if (mRecyclerView != null) {
            if (layoutManager != null && layoutManager.findFirstVisibleItemPosition() > 5) {
                mRecyclerView.scrollToPosition(5)
            }
            mRecyclerView.smoothScrollToPosition(0)
        }

    }*/

    override fun onRequestPhotosSuccess(call: Call<List<Photo>>, response: Response<List<Photo>>) {
        Log.d(TAG, response.code().toString())
        mSwipeRefreshLayout setRefresh false
        if (!loadMore) PopupUtils.showToast(context, "Your photos :)", Toast.LENGTH_SHORT)
        if (response.isSuccessful) {
            if (response.body() != null) {
                mPage++
                loadMore = false
                mPhotosList.clear()
                mPhotosList.addAll(ArrayList(response.body()!!))
                updateAdapter(mPhotosList)
                mRecyclerView.smoothScrollToPosition(mPhotoAdapter.itemCount.minus(mPhotosList.size - 1))
                Tools.visibleViews(mRecyclerView)
                Tools.inVisibleViews(lytNetworkError, lytHttpError, type = Tools.GONE)
            }
        } else {
            mSwipeRefreshLayout setRefresh false
            loadMore = false
            if (mPhotosList.isEmpty()) {
                Tools.visibleViews(lytHttpError)
                Tools.inVisibleViews(mRecyclerView, lytNetworkError, type = Tools.GONE)
            } else snackBar = PopupUtils.showHttpErrorSnackBar(mSwipeRefreshLayout) { loadPhotos(mRecyclerView.layoutManager?.itemCount!!) }
        }
    }

    override fun onRequestPhotosFailed(call: Call<List<Photo>>, t: Throwable) {
        Log.d(TAG, t.message!!)
        mSwipeRefreshLayout setRefresh false
        loadMore = false
        if (mPhotosList.isEmpty()) {
            Tools.visibleViews(lytNetworkError)
            Tools.inVisibleViews(mRecyclerView, lytHttpError, type = Tools.GONE)
        } else snackBar = PopupUtils.showNetworkErrorSnackBar(mSwipeRefreshLayout) {
            loadPhotos(mRecyclerView.layoutManager?.itemCount!!)
        }
    }

    override fun onItemClick(photo: Photo, view: View, pos: Int, imageView: ImageView) {
        //onPhotoClick(photo, view, pos, imageView)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, imageView, ViewCompat.getTransitionName(imageView)!!)
        val intent = Intent(activity, PhotoDetailActivity::class.java)
        intent.putExtra(Preferences.PHOTO, Gson().toJson(photo))
        startActivity(intent, options.toBundle())
    }

    override fun onItemLongClick(photo: Photo, view: View, pos: Int, imageView: ImageView) {
        //onPhotoLongClick(photo, view, pos, imageView)
        PopupUtils.showToast(context, "${pos.plus(1)}", Toast.LENGTH_SHORT)
        Log.d(WallBox.TAG, "mOnItemClickListener: onItemLongClick")
    }

    override fun onUserProfileClick(photo: Photo, pos: Int, imgPhotoBy: CircularImageView) {
        //onPhotoUserProfileClick(photo, pos, imgPhotoBy)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, imgPhotoBy, ViewCompat.getTransitionName(imgPhotoBy)!!)
        val intent = Intent(activity, ProfileActivity::class.java)
        intent.putExtra(Preferences.USER, Gson().toJson(photo.user))
        startActivity(intent, options.toBundle())
    }

    /** Abstract methods */
    abstract fun loadPhotos(totalItem: Int)
    /*abstract fun onPhotoClick(photo: Photo, view: View, pos: Int, imageView: ImageView)
    abstract fun onPhotoLongClick(photo: Photo, view: View, pos: Int, imageView: ImageView)
    abstract fun onPhotoUserProfileClick(photo: Photo, pos: Int, imgPhotoBy: CircularImageView)*/
    abstract fun getView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View

}