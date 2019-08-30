package com.dilipsuthar.wallbox.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife

import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.activity.CollectionDetailActivity
import com.dilipsuthar.wallbox.adapters.CollectionAdapter
import com.dilipsuthar.wallbox.data.model.Collection
import com.dilipsuthar.wallbox.data.model.User
import com.dilipsuthar.wallbox.data.service.Services
import com.dilipsuthar.wallbox.helpers.setRefresh
import com.dilipsuthar.wallbox.preferences.Preferences
import com.dilipsuthar.wallbox.utils.PopupUtils
import com.dilipsuthar.wallbox.utils.Tools
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response

/**
 * Created by DILIP SUTHAR on 30/09/19
 */
class UserCollectionsFragment : Fragment() {
    private val TAG = "WallBox.UserC_Frag"

    companion object {
        fun newInstance(sort: String): UserCollectionsFragment {
            val fragment =  UserCollectionsFragment()

            val args = Bundle()
            args.putString(Preferences.SORT, sort)
            fragment.arguments = args

            return fragment
        }
    }

    private var mUser: User? = null
    private var mService: Services? = null
    private var mOnRequestCollectionsListener: Services.OnRequestCollectionsListener? = null
    private var mPage = 0
    private var mSort: String? = null
    private var mCollectionList: ArrayList<Collection> = ArrayList()
    private var mAdapter: CollectionAdapter? = null
    private var mOnCollectionClickListener: CollectionAdapter.OnCollectionClickListener? = null
    private var loadMore = false
    private var snackBar: Snackbar? = null

    @BindView(R.id.recycler_view) lateinit var mRecyclerView: RecyclerView
    @BindView(R.id.swipe_refresh_layout) lateinit var mSwipeRefreshLyt: SwipeRefreshLayout
    @BindView(R.id.network_error_layout) lateinit var lytNetworkError: View
    @BindView(R.id.http_error_layout) lateinit var lytHttpError: View

    /**
     * it assign user from the parent activity, --must be call
     *
     * @param user user dataModel for getting username to request collection
     */
    fun setUser(user: User?) {
        mUser = user
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSort = arguments?.getString(Preferences.SORT, WallBox.DEFAULT_SORT_COLLECTIONS)

        /** SERVICES / API's */
        mService = Services.getService()
        mOnRequestCollectionsListener = object : Services.OnRequestCollectionsListener {
            override fun onRequestCollectionsSuccess(call: Call<List<Collection>>, response: Response<List<Collection>>) {
                Log.d(CollectionsFragment.TAG, response.code().toString())

                mSwipeRefreshLyt setRefresh false
                if (!loadMore) PopupUtils.showToast(context, "Your collections :)", Toast.LENGTH_SHORT)
                if (response.code() == 200) {
                    mPage++
                    loadMore = false
                    mCollectionList.clear()
                    mCollectionList.addAll(ArrayList(response.body()!!))
                    updateAdapter(mCollectionList)
                    mRecyclerView.smoothScrollToPosition(mAdapter!!.itemCount.minus(mCollectionList.size - 1))
                    Tools.visibleViews(mRecyclerView)
                    Tools.inVisibleViews(lytNetworkError, lytHttpError, type = Tools.GONE)
                } else {
                    mSwipeRefreshLyt setRefresh false
                    loadMore = false
                    if (mCollectionList.isEmpty()) {
                        Tools.visibleViews(lytHttpError)
                        Tools.inVisibleViews(mRecyclerView, lytNetworkError, type = Tools.GONE)
                    } else PopupUtils.showHttpErrorSnackBar(mSwipeRefreshLyt) { load() }
                }
            }

            override fun onRequestCollectionsFailed(call: Call<List<Collection>>, t: Throwable) {
                Log.d(CollectionsFragment.TAG, t.message)
                mSwipeRefreshLyt setRefresh false
                loadMore = false
                if (mCollectionList.isEmpty()) {
                    Tools.visibleViews(lytNetworkError)
                    Tools.inVisibleViews(mRecyclerView, lytHttpError, type = Tools.GONE)
                } else PopupUtils.showNetworkErrorSnackBar(mSwipeRefreshLyt) { load() }
            }
        }

        /** ADAPTER LISTENER */
        mOnCollectionClickListener = object : CollectionAdapter.OnCollectionClickListener {
            override fun onCollectionClick(collection: Collection, view: View, pos: Int) {
                // TODO: open collection's detail activity here
                val intent = Intent(activity!!, CollectionDetailActivity::class.java)
                intent.putExtra(Preferences.COLLECTION, Gson().toJson(collection))
                startActivity(intent)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        retainInstance = true
        val view =  inflater.inflate(R.layout.fragment_user_collections, container, false)
        ButterKnife.bind(this, view)

        /** Recycler View */
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setItemViewCacheSize(5)
        mAdapter = CollectionAdapter(ArrayList(), context, mOnCollectionClickListener)
        mRecyclerView.adapter = mAdapter

        mPage = 1
        load()

        // RecyclerView listener
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                /** check for first & last item position */
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItem = layoutManager.itemCount
                val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()
                val endHasBeenReached = lastVisible.plus(1) >= totalItem   // Load more photos on last item

                if (totalItem > 0 && endHasBeenReached && !loadMore) {
                    if (totalItem != mUser!!.total_collections) {  // Check if more photos available OR not
                        //loadMore = true
                        load()
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        // Swipe listener
        mSwipeRefreshLyt.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(context!!, R.color.colorAccent))
        mSwipeRefreshLyt.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.white))
        mSwipeRefreshLyt.setOnRefreshListener {
            mPage = 1
            mCollectionList.clear()
            load()
            mAdapter = CollectionAdapter(ArrayList(), context, mOnCollectionClickListener)
            mRecyclerView.adapter = mAdapter
        }

        /** Event listener */
        lytNetworkError.setOnClickListener {
            load()
            it.visibility = View.GONE
        }
        lytHttpError.setOnClickListener {
            load()
            it.visibility = View.GONE
        }

        return view
    }

    /** methods */
    private fun load() {
        Log.d(CollectionsFragment.TAG, "load: called >>>>>>>>>>")
        mSwipeRefreshLyt setRefresh true
        loadMore = true
        if (snackBar != null) snackBar?.dismiss()

        if (mUser != null) {
            mService?.requestUserCollections(mUser?.username!!, mPage, WallBox.DEFAULT_PER_PAGE, mOnRequestCollectionsListener)
        } else {
            Tools.visibleViews(lytHttpError)
            Tools.inVisibleViews(mRecyclerView, lytNetworkError, type = Tools.GONE)
            mSwipeRefreshLyt setRefresh false
        }
    }

    /*private fun loadMore() {
        when (mSort) {
            "all" -> mService?.requestCollections(mPage++, WallBox.DEFAULT_PER_PAGE, mOnRequestCollectionsListener)
            "featured" -> mService?.requestFeaturedCollections(mPage, WallBox.DEFAULT_PER_PAGE, mOnRequestCollectionsListener)
            "curated" -> mService?.requestCuratedCollections(mPage, WallBox.DEFAULT_PER_PAGE, mOnRequestCollectionsListener)
        }
    }*/

    private fun updateAdapter(collections: ArrayList<Collection>) {
        Log.d(CollectionsFragment.TAG, "updateAdapter: called >>>>>>>>>>")

        mAdapter?.addAll(collections)
    }

}
