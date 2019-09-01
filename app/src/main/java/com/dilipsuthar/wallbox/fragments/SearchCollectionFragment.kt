package com.dilipsuthar.wallbox.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
import com.dilipsuthar.wallbox.data.model.SearchCollections
import com.dilipsuthar.wallbox.data.service.Services
import com.dilipsuthar.wallbox.helpers.setRefresh
import com.dilipsuthar.wallbox.preferences.Preferences
import com.dilipsuthar.wallbox.utils.PopupUtils
import com.dilipsuthar.wallbox.utils.Tools
import com.dilipsuthar.wallbox.utils.itemDecorater.VerticalSpacingItemDecorator
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response

/**
 * Created by DILIP SUTHAR on 01/09/19
 */
class SearchCollectionFragment : Fragment() {

    private val TAG = "WallBox.SearchPhoto"

    companion object {
        fun newInstance(query: String?): SearchCollectionFragment {
            val fragment = SearchCollectionFragment()

            val args = Bundle()
            args.putString(Preferences.SEARCH_QUERY, query)
            fragment.arguments = args

            return fragment
        }
    }

    private var mQuery: String? = null
    private lateinit var mService: Services
    private lateinit var mOnSearchCollectionsListener: Services.OnSearchCollectionsListener
    private var mPage = 0
    private lateinit var mCollectionAdapter: CollectionAdapter
    private lateinit var mOnCollectionClickListener: CollectionAdapter.OnCollectionClickListener
    private var mCollectionList: ArrayList<Collection> = ArrayList()
    private var loadMore: Boolean = false
    private var snackBar: Snackbar? = null

    @BindView(R.id.recycler_view) lateinit var mRecyclerView: RecyclerView
    @BindView(R.id.swipe_refresh_layout) lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    @BindView(R.id.network_error_layout) lateinit var lytNetworkError: LinearLayout
    @BindView(R.id.http_error_layout) lateinit var lytHttpError: LinearLayout
    @BindView(R.id.no_items_layout) lateinit var lytNoItems: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mQuery = arguments!!.getString(Preferences.SEARCH_QUERY, "")

        /** SERVICES / API */
        mService = Services.getService()

        /** Listeners */
        // API request listener
        mOnSearchCollectionsListener = object : Services.OnSearchCollectionsListener {
            override fun onSearchCollectionsSuccess(call: Call<SearchCollections>, response: Response<SearchCollections>) {

                Log.d(TAG, response.code().toString())
                mSwipeRefreshLayout setRefresh false
                if (!loadMore) PopupUtils.showToast(context, "Your photos :)", Toast.LENGTH_SHORT)
                if (response.code() == 200) {
                    if (response.body()!!.results.isNotEmpty()) {
                        mPage++
                        loadMore = false
                        mCollectionList.clear()
                        mCollectionList.addAll(ArrayList(response.body()!!.results))
                        updateAdapter(mCollectionList)
                        mRecyclerView.smoothScrollToPosition(mCollectionAdapter.itemCount.minus(mCollectionList.size))
                        Tools.visibleViews(mRecyclerView)
                        Tools.inVisibleViews(lytNetworkError, lytHttpError, lytNoItems, type = Tools.GONE)
                    } else if (response.body()!!.results.isEmpty() && mCollectionList.isEmpty()) {
                        Tools.visibleViews(lytNoItems)
                    }
                } else {
                    mSwipeRefreshLayout setRefresh false
                    loadMore = false
                    if (mCollectionList.isEmpty()) {
                        Tools.visibleViews(lytHttpError)
                        Tools.inVisibleViews(mRecyclerView, lytNetworkError, lytNoItems, type = Tools.GONE)
                    } else snackBar = PopupUtils.showHttpErrorSnackBar(mSwipeRefreshLayout) { load() }
                }

            }

            override fun onSearchCollectionsFailed(call: Call<SearchCollections>, t: Throwable) {

                Log.d(TAG, t.message!!)
                mSwipeRefreshLayout setRefresh false
                loadMore = false
                if (mCollectionList.isEmpty()) {
                    Tools.visibleViews(lytNetworkError)
                    Tools.inVisibleViews(mRecyclerView, lytHttpError, lytNoItems, type = Tools.GONE)
                } else snackBar = PopupUtils.showNetworkErrorSnackBar(mSwipeRefreshLayout) { load() }

            }
        }

        // Adapter listener
        mOnCollectionClickListener = object : CollectionAdapter.OnCollectionClickListener {
            override fun onCollectionClick(collection: Collection, view: View, pos: Int) {
                val intent = Intent(activity!!, CollectionDetailActivity::class.java)
                intent.putExtra(Preferences.COLLECTION, Gson().toJson(collection))
                startActivity(intent)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true
        val view =  inflater.inflate(R.layout.fragment_search_collection, container, false)
        ButterKnife.bind(this, view)

        /** Recycler View */
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setItemViewCacheSize(5)
        mCollectionAdapter = CollectionAdapter(ArrayList(), context, mOnCollectionClickListener)
        mRecyclerView.adapter = mCollectionAdapter

        mPage = 1
        load()

        /** Views listeners */
        // RecyclerView listener
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                    load()
                }

            }
        })

        // Swipe refresh listener
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(context!!, R.color.colorAccent))
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.white))
        mSwipeRefreshLayout.setOnRefreshListener {
            mPage = 1
            mCollectionList.clear()
            load()
            mCollectionAdapter = CollectionAdapter(ArrayList(), context, mOnCollectionClickListener)
            mRecyclerView.adapter = mCollectionAdapter
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

    /** Methods */
    private fun load() {
        if (mQuery != "") {
            Tools.inVisibleViews(lytNoItems, type = Tools.GONE)
            mSwipeRefreshLayout setRefresh true
            loadMore = true
            if (snackBar != null) snackBar?.dismiss()
            mService.searchCollections(mQuery!!, mPage, WallBox.DEFAULT_PER_PAGE, mOnSearchCollectionsListener)
        } else {
            mSwipeRefreshLayout setRefresh false
        }
    }

    private fun updateAdapter(collections: ArrayList<Collection>) {
        mCollectionAdapter.addAll(collections)
    }

}
