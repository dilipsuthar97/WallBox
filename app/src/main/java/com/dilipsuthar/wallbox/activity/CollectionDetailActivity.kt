package com.dilipsuthar.wallbox.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.adapters.PhotoAdapter
import com.dilipsuthar.wallbox.data.model.Collection
import com.dilipsuthar.wallbox.data.model.Photo
import com.dilipsuthar.wallbox.data.service.Services
import com.dilipsuthar.wallbox.helpers.loadUrl
import com.dilipsuthar.wallbox.helpers.setRefresh
import com.dilipsuthar.wallbox.preferences.Preferences
import com.dilipsuthar.wallbox.utils.*
import com.dilipsuthar.wallbox.utils.itemDecorater.VerticalSpacingItemDecorator
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.mikhaellopez.circularimageview.CircularImageView
import retrofit2.Call
import retrofit2.Response
/**
 * Created by DILIP SUTHAR 05/06/19
 */
class CollectionDetailActivity : BaseActivity() {
    private val TAG = "WallBox.CollectionDetailAct"

    private var mPage = 0
    private var mCollection: Collection? = null
    private var mPhotoList: ArrayList<Photo> = ArrayList()
    private var mServices: Services? = null
    private var mOnRequestPhotosListener: Services.OnRequestPhotosListener? = null

    private var mPhotoAdapter: PhotoAdapter? = null
    private var mOnItemClickListener: PhotoAdapter.OnItemClickListener? = null

    private var loadMore = false
    private var snackBar: Snackbar? = null

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.btn_profile) lateinit var btnProfile: LinearLayout
    @BindView(R.id.img_user_profile) lateinit var imgUserProfile: CircularImageView
    @BindView(R.id.tv_photo_by) lateinit var tvPhotoBy: TextView

    @BindView(R.id.swipe_refresh_lyt_collection_photo) lateinit var mSwipeRefreshView: SwipeRefreshLayout
    @BindView(R.id.recycler_view_collection_photo) lateinit var mRecyclerView: RecyclerView
    @BindView(R.id.network_error_layout) lateinit var netWorkErrorLyt: View
    @BindView(R.id.http_error_layout) lateinit var httpErrorLyt: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_detail)
        ButterKnife.bind(this)
        mServices = Services.getService()

        /** Set Collection data on Toolbar */
        mCollection = Gson().fromJson(intent.getStringExtra(Preferences.COLLECTION), Collection::class.java)
        mCollection?.let {
            imgUserProfile.loadUrl(
                it.user.profile_image.medium,
                R.drawable.placeholder_profile,
                R.drawable.placeholder_profile)
            tvPhotoBy.text = "${resources.getString(R.string.wallpaper_by)} ${it.user.first_name} ${it.user.last_name}"
        }

        /** Listeners */
        // API request listener
        mOnRequestPhotosListener = object : Services.OnRequestPhotosListener {
            override fun onRequestPhotosSuccess(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                mSwipeRefreshView setRefresh false
                if (response.isSuccessful) {

                    if (loadMore) PopupUtils.showToast(applicationContext, resources.getString(R.string.msg_more_photos), Toast.LENGTH_SHORT)
                    mPage++
                    loadMore = false
                    mPhotoList.clear()
                    mPhotoList.addAll(ArrayList(response.body()!!))
                    updateAdapter(mPhotoList)
                    Tools.visibleViews(mRecyclerView)
                    Tools.inVisibleViews(netWorkErrorLyt, httpErrorLyt, type = Tools.GONE)

                } else {
                    mSwipeRefreshView setRefresh false
                    loadMore = false
                    if (mPhotoList.isEmpty()) {
                        Tools.visibleViews(httpErrorLyt)
                        Tools.inVisibleViews(mRecyclerView, netWorkErrorLyt, type = Tools.GONE)
                    } else snackBar = PopupUtils.showHttpErrorSnackBar(mSwipeRefreshView) { load() }
                }
            }

            override fun onRequestPhotosFailed(call: Call<List<Photo>>, t: Throwable) {
                Log.d(TAG, t.message!!)
                mSwipeRefreshView setRefresh false
                loadMore = false
                if (mPhotoList.isEmpty()) {
                    Tools.visibleViews(netWorkErrorLyt)
                    Tools.inVisibleViews(mRecyclerView, httpErrorLyt, type = Tools.GONE)
                } else snackBar = PopupUtils.showNetworkErrorSnackBar(mSwipeRefreshView) { load() }
            }
        }

        // Adapter listener
        mOnItemClickListener = object : PhotoAdapter.OnItemClickListener {
            override fun onItemClick(photo: Photo, view: View, pos: Int, imageView: ImageView) {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@CollectionDetailActivity, imageView, ViewCompat.getTransitionName(imageView)!!)
                val intent = Intent(this@CollectionDetailActivity, PhotoDetailActivity::class.java)
                intent.putExtra(Preferences.PHOTO, Gson().toJson(photo))
                startActivity(intent, options.toBundle())
            }

            override fun onItemLongClick(photo: Photo, view: View, pos: Int, imageView: ImageView) {
                PopupUtils.showToast(applicationContext, pos.toString(), Toast.LENGTH_SHORT)
            }

            override fun onUserProfileClick(photo: Photo, pos: Int, imgPhotoBy: CircularImageView) {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@CollectionDetailActivity, imgPhotoBy, ViewCompat.getTransitionName(imgPhotoBy)!!)
                val intent = Intent(this@CollectionDetailActivity, UserActivity::class.java)
                intent.putExtra(Preferences.USER, Gson().toJson(photo.user))
                startActivity(intent, options.toBundle())
            }
        }

        // Swipe refresh listener
        mSwipeRefreshView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorAccent))
        mSwipeRefreshView.setColorSchemeColors(ContextCompat.getColor(this, R.color.white))
        mSwipeRefreshView.setOnRefreshListener {
            mPage = 1
            mPhotoList.clear()
            load()
            mPhotoAdapter = PhotoAdapter(ArrayList(), "list", this, mOnItemClickListener)
            mRecyclerView.adapter = mPhotoAdapter
        }

        // Recycler view listener
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItem = layoutManager.itemCount
                val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()

                val endHasBeenReached = lastVisible.plus(1) >= totalItem
                if (totalItem > 0 && endHasBeenReached && !loadMore)
                    if (totalItem != mCollection!!.total_photos) {  // Check if more photos available OR not
                        //loadMore = true
                        load()
                    } else PopupUtils.showToast(applicationContext, resources.getString(R.string.msg_no_more_photos), Toast.LENGTH_SHORT)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            }
        })

        // OnClick listener
        netWorkErrorLyt.setOnClickListener {
            load()
            it.visibility = View.GONE
        }
        httpErrorLyt.setOnClickListener {
            load()
            it.visibility = View.GONE
        }
        btnProfile.setOnClickListener {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imgUserProfile, ViewCompat.getTransitionName(imgUserProfile)!!)
            val intent = Intent(this, UserActivity::class.java)
            intent.putExtra(Preferences.USER, Gson().toJson(mCollection?.user))
            startActivity(intent, options.toBundle())
        }

        initToolbar()
        initComponent()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    /** Methods */
    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = mCollection?.title
        supportActionBar?.subtitle = "${mCollection?.total_photos} ${resources.getString(R.string.wallpapers)}"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.changeNavigationIconColor(toolbar, ContextCompat.getColor(applicationContext, R.color.colorAccent))
    }

    private fun initComponent() {

        /** Recycler View */
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(VerticalSpacingItemDecorator(22))
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setItemViewCacheSize(5)
        mPhotoAdapter = PhotoAdapter(ArrayList(), "list", this, mOnItemClickListener)
        mRecyclerView.adapter = mPhotoAdapter

        /** First load request */
        mPage = 1
        load()
    }

    private fun load() {
        mSwipeRefreshView setRefresh true
        loadMore = true
        if (snackBar != null) snackBar?.dismiss()
        if (mCollection!!.curated)
            mServices?.requestCuratedCollectionPhotos(mCollection?.id.toString(), mPage, WallBox.DEFAULT_PER_PAGE, mOnRequestPhotosListener)
        else
            mServices?.requestCollectionPhotos(mCollection?.id.toString(), mPage, WallBox.DEFAULT_PER_PAGE, mOnRequestPhotosListener)
    }

    /**
     * Update PhotoAdapter when new photos fetched
     *
     * @param photos List of photos to update adapter
     */
    private fun updateAdapter(photos: ArrayList<Photo>) {
        mPhotoAdapter?.addAll(photos)
    }
}
