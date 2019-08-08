package com.dilipsuthar.wallbox.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife

import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.adapters.CollectionAdapter
import com.dilipsuthar.wallbox.data.model.Collection
import com.dilipsuthar.wallbox.data.service.Services
import com.dilipsuthar.wallbox.preferences.Preferences
import com.dilipsuthar.wallbox.utils.Popup
import com.dilipsuthar.wallbox.utils.Tools
import com.dilipsuthar.wallbox.utils.setRefresh
import retrofit2.Call
import retrofit2.Response

/**
* Created by DILIP SUTHAR on 28/07/19
*/

class CollectionsFragment : Fragment() {

    companion object {
        const val TAG = "WallBox.CollectionsFragment"

        fun newInstance(sort: String): CollectionsFragment {
            val fragment = CollectionsFragment()

            val args = Bundle()
            args.putString(Preferences.SORT, sort)
            fragment.arguments = args

            return fragment
        }
    }

    // VARS
    private var mService: Services? = null
    private var mOnRequestCollectionsListener: Services.OnRequestCollectionsListener? = null
    private var mPage = 0
    private var mSort: String? = null
    private var mCollectionList: ArrayList<Collection> = ArrayList()
    private var mAdapter: CollectionAdapter? = null
    private var mOnCollectionClickListener: CollectionAdapter.OnCollectionClickListener? = null
    private var loadMore = false

    // VIEWS
    @BindView(R.id.collections_list) lateinit var mRecyclerView: RecyclerView
    @BindView(R.id.collections_swipe_refresh_layout) lateinit var mSwipeRefreshView: SwipeRefreshLayout

    /** Main method */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSort = arguments?.getString(Preferences.SORT, WallBox.DEFAULT_SORT_COLLECTIONS)

        /** SERVICES / API's */
        mService = Services.getService()
        mOnRequestCollectionsListener = object : Services.OnRequestCollectionsListener {
            override fun onRequestCollectionsSuccess(call: Call<List<Collection>>, response: Response<List<Collection>>) {
                Log.d(TAG, response.code().toString())

                mSwipeRefreshView setRefresh false
                if (!loadMore) Popup.showToast(context, "Updated collections", Toast.LENGTH_SHORT)
                if (response.isSuccessful) {
                    loadMore = false
                    mCollectionList.clear()
                    mCollectionList.addAll(ArrayList(response.body()!!))
                    updateAdapter(mCollectionList)
                    Tools.visibleViews(mRecyclerView)
                } else {
                    //Dialog.showErrorDialog(context, Dialog.HTTP_ERROR, mPhotosList, ::load, ::loadMore)
                }
            }

            override fun onRequestCollectionsFailed(call: Call<List<Collection>>, t: Throwable) {
                Log.d(WallBox.TAG, t.message)
                mSwipeRefreshView setRefresh false
                //Dialog.showErrorDialog(context, Dialog.NETWORK_ERROR, mPhotosList, ::load, ::loadMore)
            }
        }

        /** ADAPTER LISTENER */
        mOnCollectionClickListener = object : CollectionAdapter.OnCollectionClickListener {
            override fun onCollectionClick(collection: Collection, view: View, pos: Int, imageView: ImageView) {
                Popup.showToast(context, "$pos >> ${collection.title}", Toast.LENGTH_SHORT)
            }
        }
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

        mPage = 1
        load()

        // RecyclerView listener
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                /** check for first & last item position */
                val layoutManager = LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
                val totalItem = layoutManager?.itemCount
                val lastVisible = layoutManager?.findLastVisibleItemPosition()
                val endHasBeenReached = lastVisible?.plus(2)!! >= totalItem!!   // Load more photos on last item
                if (totalItem > 0 && endHasBeenReached && !loadMore) {
                    loadMore = true
                    mSwipeRefreshView setRefresh true
                    loadMore()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        // Swipe listener
        mSwipeRefreshView.setOnRefreshListener {
            mPage = 1
            mCollectionList.clear()
            load()
        }

        return view
    }

    /** methods */
    private fun load() {
        Log.d(TAG, "load: called >>>>>>>>>>")

        mSwipeRefreshView setRefresh true
        when (mSort) {
            "all" -> mService?.requestCollections(mPage++, WallBox.DEFAULT_PER_PAGE, mOnRequestCollectionsListener)
            "featured" -> mService?.requestFeaturedCollections(mPage++, WallBox.DEFAULT_PER_PAGE, mOnRequestCollectionsListener)
            "curated" -> mService?.requestCuratedCollections(mPage++, WallBox.DEFAULT_PER_PAGE, mOnRequestCollectionsListener)
        }
        mAdapter = CollectionAdapter(ArrayList(), context, mOnCollectionClickListener)
        mRecyclerView.adapter = mAdapter
    }

    private fun loadMore() {
        when (mSort) {
            "all" -> mService?.requestCollections(mPage++, WallBox.DEFAULT_PER_PAGE, mOnRequestCollectionsListener)
            "featured" -> mService?.requestFeaturedCollections(mPage++, WallBox.DEFAULT_PER_PAGE, mOnRequestCollectionsListener)
            "curated" -> mService?.requestCuratedCollections(mPage++, WallBox.DEFAULT_PER_PAGE, mOnRequestCollectionsListener)
        }
    }

    private fun updateAdapter(collections: ArrayList<Collection>) {
        Log.d(TAG, "updateAdapter: called >>>>>>>>>>")

        mAdapter?.addAll(collections)
    }

}
