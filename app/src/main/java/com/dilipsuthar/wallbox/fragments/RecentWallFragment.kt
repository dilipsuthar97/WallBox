package com.dilipsuthar.wallbox.fragments

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.Toast
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
import com.dilipsuthar.wallbox.utils.Popup
import com.dilipsuthar.wallbox.utils.Tools
import com.dilipsuthar.wallbox.viewmodels.WallpaperListViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mlsdev.animatedrv.AnimatedRecyclerView
import retrofit2.Call
import retrofit2.Response

/**
 * Created by Dilip on 23/07/19
 */

class RecentWallFragment : Fragment() {

    companion object {
        fun newInstance(sort: String): RecentWallFragment {
            val fragment = RecentWallFragment()

            val args = Bundle()
            args.putString(Preferences.SORT, sort)
            fragment.arguments = args

            return fragment
        }
    }

    // VARS
    private val NETWORK_ERROR = 0
    private val HTTP_ERROR = 1
    private var mWallpaperListViewModel: WallpaperListViewModel? = null

    private var mService: PhotoService? = null
    private var mOnRequestPhotosListener: PhotoService.OnRequestPhotosListener? = null
    private var mPage: Int = 0
    private var mSort: String? = null
    private var mPhotosList: ArrayList<Photo> = ArrayList()
    private var mPhotoAdapter: PhotoAdapter? = null
    private var mOnItemClickListener: PhotoAdapter.OnItemClickListener? = null
    private var mDialog: Dialog? = null
    private var loadMore: Boolean = false


    // VIEWS
    @BindView(R.id.recent_wallpaper_list) lateinit var mRecyclerView: RecyclerView
    @BindView(R.id.progress) lateinit var mProgressView: LottieAnimationView
    @BindView(R.id.recent_swipe_refresh_layout) lateinit var mSwipRefreshView: SwipeRefreshLayout
    @BindView(R.id.fab_scroll_to_top) lateinit var mFabScrollToTop: FloatingActionButton

    // MAIN FUNCTIONS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WallBox.getInstance())
        mSort = arguments?.getString(Preferences.SORT, "latest")

        // SERVICES / API
        mService = PhotoService.getService()
        mOnRequestPhotosListener = object : PhotoService.OnRequestPhotosListener {
            override fun onRequestPhotosSuccess(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                Log.d(WallBox.TAG, response.code().toString())
                if (mSwipRefreshView.isRefreshing) {
                    mSwipRefreshView.isRefreshing = false
                    Popup.showToast(context, "Updated photos", Toast.LENGTH_SHORT)
                }
                if (response.isSuccessful) {
                    loadMore = false
                    mPhotoAdapter?.removeFooter()
                    mPhotosList.clear()
                    mPhotosList.addAll(ArrayList(response.body()!!))
                    updateAdapter(mPhotosList)
                    Tools.inVisibleViews(mProgressView as View, type = 1)
                    Tools.visibleViews(mRecyclerView)
                } else {
                    //mPhotoAdapter?.removeFooter()
                    Tools.inVisibleViews( mProgressView as View, type = 1)
                    showErrorDialog(HTTP_ERROR)
                }
            }

            override fun onRequestPhotosFailed(call: Call<List<Photo>>, t: Throwable) {
                Log.d(WallBox.TAG, t.message)
                mSwipRefreshView.isRefreshing = false
                //mPhotoAdapter?.removeFooter()
                showErrorDialog(NETWORK_ERROR)
                Tools.inVisibleViews(mProgressView as View, type = 1)
            }
        }

        // ADAPTER LISTENERS
        mOnItemClickListener = object : PhotoAdapter.OnItemClickListener {
            override fun onItemClick(photo: Photo, view: View, pos: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        val view = inflater.inflate(R.layout.fragment_recent_wall, container, false)
        ButterKnife.bind(this, view)

        initComponent()

        mPage = 1
        load()

        return view
    }

    // METHODS
    private fun initComponent() {
        // Recycler View
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.setItemViewCacheSize(5)
        //mRecyclerView.addItemDecoration(VerticalSpacingItemDecorator(5))
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
                if (atTopReached)
                    mFabScrollToTop.hide()

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
                        mFabScrollToTop.show()
                    } else {
                        mFabScrollToTop.hide()
                    }
                }
            }

        })


        // Swipe Refresh Layout
        mSwipRefreshView.setOnRefreshListener {
            mPage = 1
            mPhotosList.clear()
            load()
        }

        // Fab
        mFabScrollToTop.setOnClickListener {
            scrollToTop()
        }
    }

    private fun load() {
        Tools.visibleViews(mProgressView)
        //Tools.inVisibleViews(mRecyclerView, mNetworkErrorView, mHttpErrorView, type = 1)
        mService?.requestPhotos(mPage++, WallBox.DEFAULT_PER_PAGE, mSort!!, mOnRequestPhotosListener)
        mPhotoAdapter = PhotoAdapter(ArrayList(), context!!, mOnItemClickListener)
        mRecyclerView.adapter = mPhotoAdapter
    }

    private fun loadMore() {
        mService?.requestPhotos(mPage++, WallBox.DEFAULT_PER_PAGE, mSort!!, mOnRequestPhotosListener)
    }

    private fun updateAdapter(photos: ArrayList<Photo>) {
        mPhotoAdapter?.addAll(photos)
    }

    private fun scrollToTop() {
        val layoutManager = mRecyclerView.layoutManager as LinearLayoutManager
        if (mRecyclerView != null) {
            if (layoutManager != null && layoutManager.findFirstVisibleItemPosition() > 5) {
                mRecyclerView.scrollToPosition(5)
            }
            mRecyclerView.smoothScrollToPosition(0)
        }

    }

    /*private fun subscribeObservers() {
        mWallpaperListViewModel = ViewModelProviders.of(activity!!).get(WallpaperListViewModel::class.java)

    }*/

    private fun showErrorDialog(error_type: Int) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        when (error_type) {
            0 -> dialog.setContentView(R.layout.dialog_network_error)
            1 -> dialog.setContentView(R.layout.dialog_http_error)
        }
        dialog.setCancelable(false)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = lp

        (dialog.findViewById<ImageButton>(R.id.btn_dismiss)).setOnClickListener {
            dialog.cancel()
        }

        (dialog.findViewById<MaterialButton>(R.id.btn_retry)).setOnClickListener {
            dialog.cancel()
            if (mPhotosList.isEmpty())
                load()
            else
                loadMore()
        }

        dialog.show()
    }

}
