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
import com.dilipsuthar.wallbox.data_source.model.Collection
import com.dilipsuthar.wallbox.data_source.service.Services
import com.dilipsuthar.wallbox.preferences.Prefs
import com.dilipsuthar.wallbox.utils.PopupUtils
import com.dilipsuthar.wallbox.utils.Tools
import com.dilipsuthar.wallbox.helpers.setRefresh
import com.dilipsuthar.wallbox.utils.ThemeUtils
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
/**
* Created by DILIP SUTHAR on 28/07/19
*/
class CollectionsFragment : Fragment(), Services.OnRequestCollectionsListener, CollectionAdapter.OnCollectionClickListener{

    companion object {
        private val TAG = CollectionsFragment::class.java.simpleName

        fun newInstance(sort: String): CollectionsFragment {
            val fragment = CollectionsFragment()

            val args = Bundle()
            args.putString(Prefs.SORT, sort)
            fragment.arguments = args

            return fragment
        }
    }

    private var mService: Services? = null
    private var mPage = 0
    private var mSort: String? = null
    private var mCollectionList: ArrayList<Collection> = ArrayList()
    private var mAdapter: CollectionAdapter? = null
    private var loadMore = false
    private var snackBar: Snackbar? = null

    @BindView(R.id.collections_list) lateinit var mRecyclerView: RecyclerView
    @BindView(R.id.collections_swipe_refresh_layout) lateinit var mSwipeRefreshView: SwipeRefreshLayout
    @BindView(R.id.network_error_layout) lateinit var netWorkErrorLyt: View
    @BindView(R.id.http_error_layout) lateinit var httpErrorLyt: View

    /** Main method */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSort = arguments?.getString(Prefs.SORT, WallBox.DEFAULT_SORT_COLLECTIONS)

        /** SERVICES / API's */
        mService = Services.getService()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView: called >>>>>")

        retainInstance = true
        val view = inflater.inflate(R.layout.fragment_collection_wall, container, false)
        ButterKnife.bind(this, view)

        /** Recycler View */
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setItemViewCacheSize(5)
        mAdapter = CollectionAdapter(ArrayList(), context, this)
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
                    //loadMore = true
                    load()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        // Swipe listener
        mSwipeRefreshView.setProgressBackgroundColorSchemeColor(ThemeUtils.getThemeAttrColor(context!!, R.attr.colorPrimary))
        mSwipeRefreshView.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.white))
        mSwipeRefreshView.setOnRefreshListener {
            mPage = 1
            mCollectionList.clear()
            load()
            mAdapter = CollectionAdapter(ArrayList(), context, this)
            mRecyclerView.adapter = mAdapter
        }

        /** Event listener */
        netWorkErrorLyt.setOnClickListener {
            load()
            it.visibility = View.GONE
        }
        httpErrorLyt.setOnClickListener {
            load()
            it.visibility = View.GONE
        }

        return view
    }

    /** methods */
    private fun load() {
        Log.d(TAG, "load: called >>>>>>>>>>")
        mSwipeRefreshView setRefresh true
        loadMore = true
        if (snackBar != null) snackBar?.dismiss()
        when (mSort) {
            "all" -> mService?.requestCollections(mPage++, WallBox.DEFAULT_PER_PAGE, this)
            "featured" -> mService?.requestFeaturedCollections(mPage, WallBox.DEFAULT_PER_PAGE, this)
            "curated" -> mService?.requestCuratedCollections(mPage, WallBox.DEFAULT_PER_PAGE, this)
        }
    }

    private fun loadMore() {
        when (mSort) {
            "all" -> mService?.requestCollections(mPage++, WallBox.DEFAULT_PER_PAGE, this)
            "featured" -> mService?.requestFeaturedCollections(mPage, WallBox.DEFAULT_PER_PAGE, this)
            "curated" -> mService?.requestCuratedCollections(mPage, WallBox.DEFAULT_PER_PAGE, this)
        }
    }

    private fun updateAdapter(collections: ArrayList<Collection>) {
        Log.d(TAG, "updateAdapter: called >>>>>>>>>>")

        mAdapter?.addAll(collections)
    }

    override fun onRequestCollectionsSuccess(call: Call<List<Collection>>, response: Response<List<Collection>>) {
        Log.d(TAG, response.code().toString())

        mSwipeRefreshView setRefresh false
        if (!loadMore) PopupUtils.showToast(context, "Your collections :)", Toast.LENGTH_SHORT)
        if (response.isSuccessful) {
            mPage++
            loadMore = false
            mCollectionList.clear()
            mCollectionList.addAll(ArrayList(response.body()!!))
            updateAdapter(mCollectionList)
            mRecyclerView.smoothScrollToPosition(mAdapter!!.itemCount.minus(29))
            Tools.visibleViews(mRecyclerView)
            Tools.inVisibleViews(netWorkErrorLyt, httpErrorLyt, type = Tools.GONE)
        } else {
            mSwipeRefreshView setRefresh false
            loadMore = false
            if (mCollectionList.isEmpty()) {
                Tools.visibleViews(httpErrorLyt)
                Tools.inVisibleViews(mRecyclerView, netWorkErrorLyt, type = Tools.GONE)
            } else PopupUtils.showHttpErrorSnackBar(mSwipeRefreshView) { load() }
        }
    }

    override fun onRequestCollectionsFailed(call: Call<List<Collection>>, t: Throwable) {
        Log.d(TAG, t.message)
        mSwipeRefreshView setRefresh false
        loadMore = false
        if (mCollectionList.isEmpty()) {
            Tools.visibleViews(netWorkErrorLyt)
            Tools.inVisibleViews(mRecyclerView, httpErrorLyt, type = Tools.GONE)
        } else PopupUtils.showNetworkErrorSnackBar(mSwipeRefreshView) { load() }
    }

    override fun onCollectionClick(collection: Collection, view: View, pos: Int) {
        val intent = Intent(activity!!, CollectionDetailActivity::class.java)
        intent.putExtra(Prefs.COLLECTION, Gson().toJson(collection))
        startActivity(intent)
    }

}
