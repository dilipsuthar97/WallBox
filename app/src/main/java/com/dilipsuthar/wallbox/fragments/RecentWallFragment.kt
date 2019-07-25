package com.dilipsuthar.wallbox.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
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
import com.dilipsuthar.wallbox.utils.Tools
import com.dilipsuthar.wallbox.viewmodels.WallpaperListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mlsdev.animatedrv.AnimatedRecyclerView
import retrofit2.Call
import retrofit2.Response

/**
 * Modified by Dilip
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
    @BindView(R.id.recent_wallpaper_list) lateinit var mRecyclerView: AnimatedRecyclerView
    @BindView(R.id.progress) lateinit var mProgressView: LottieAnimationView
    @BindView(R.id.network_error_layout) lateinit var mNetworkErrorView: View
    @BindView(R.id.http_error_layout) lateinit var mHttpErrorView: View
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
                    Toast.makeText(context, "Updated photos", Toast.LENGTH_SHORT).show()
                }
                if (response.isSuccessful) {
                    loadMore = false
                    mPhotosList.clear()
                    mPhotosList.addAll(ArrayList(response.body()!!))
                    mPhotoAdapter?.removeLoader()
                    updateAdapter(mPhotosList)
                    Tools.inVisibleViews(mProgressView as View, mNetworkErrorView, mHttpErrorView, type = 1)
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

        // ADAPTER LISTENER
        mOnItemClickListener = object : PhotoAdapter.OnItemClickListener {
            override fun onItemClick(photo: Photo, view: View, pos: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemLongClick(photo: Photo, view: View, pos: Int, imageView: ImageView) {
                Toast.makeText(context, "$pos", Toast.LENGTH_SHORT).show()
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
                    //Toast.makeText(context!!, "Reached end", Toast.LENGTH_SHORT).show()
                    mPhotoAdapter?.addLoader()
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
        Toast.makeText(context!!, "Load called", Toast.LENGTH_SHORT).show()
        Tools.visibleViews(mProgressView)
        Tools.inVisibleViews(mRecyclerView, mNetworkErrorView, mHttpErrorView, type = 1)
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


    // TODO( This function is not working): Solve this
    private fun showImagePreviewDialog(photo: Photo, source: ImageView) {

        //val background: BitmapDrawable
//        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//
//        val dialogView = inflater.inflate(R.layout.dialog_photo_viewer, null) as View

        /*val imagePreview = dialogView.findViewById<ImageView>(R.id.image_photo)
        val userName = dialogView.findViewById<TextView>(R.id.text_user_name)
        val userProfile = dialogView.findViewById<CircularImageView>(R.id.image_user_profile)*/

        mDialog = Dialog(context!!)
        mDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog?.setContentView(R.layout.dialog_photo_viewer)
        mDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialog?.setCancelable(true)
        mDialog?.show()

        /*imagePreview.setImageDrawable(source.drawable.constantState!!.newDrawable())
        userName.text = photo.user.username
        Glide.with(context!!)
            .load(photo.user.profile_image.medium)
            .into(userProfile)*/

    }

}
