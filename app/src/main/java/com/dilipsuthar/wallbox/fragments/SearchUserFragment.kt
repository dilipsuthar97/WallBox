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
import com.dilipsuthar.wallbox.activity.UserActivity
import com.dilipsuthar.wallbox.adapters.UserAdapter
import com.dilipsuthar.wallbox.data_source.model.SearchUsers
import com.dilipsuthar.wallbox.data_source.model.User
import com.dilipsuthar.wallbox.data_source.Services
import com.dilipsuthar.wallbox.helpers.setRefresh
import com.dilipsuthar.wallbox.preferences.Preferences
import com.dilipsuthar.wallbox.utils.PopupUtils
import com.dilipsuthar.wallbox.utils.Tools
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response

/**
 * Created by DILIP SUTHAR on 01/09/19
 */
class SearchUserFragment : Fragment() {
    private val TAG = "WallBox.SearchUser"

    companion object {
        fun newInstance(query: String?): SearchUserFragment {
            val fragment = SearchUserFragment()

            val args = Bundle()
            args.putString(Preferences.SEARCH_QUERY, query)
            fragment.arguments = args

            return fragment
        }
    }

    private var mQuery: String? = null
    private lateinit var mService: Services
    private lateinit var mOnSearchUsersListener: Services.OnSearchUsersListener
    private var mPage = 0
    private lateinit var mUserAdapter: UserAdapter
    private lateinit var mOnUserClickListener: UserAdapter.OnUserClickListener
    private var mUserList: ArrayList<User> = ArrayList()
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
        mOnSearchUsersListener = object : Services.OnSearchUsersListener {
            override fun onSearchUsersSuccess(call: Call<SearchUsers>, response: Response<SearchUsers>) {

                Log.d(TAG, response.code().toString())
                mSwipeRefreshLayout setRefresh false
                if (!loadMore) PopupUtils.showToast(context, "Your photos :)", Toast.LENGTH_SHORT)
                if (response.code() == 200) {
                    if (response.body()!!.results.isNotEmpty()) {
                        mPage++
                        loadMore = false
                        mUserList.clear()
                        mUserList.addAll(ArrayList(response.body()!!.results))
                        updateAdapter(mUserList)
                        mRecyclerView.smoothScrollToPosition(mUserAdapter.itemCount.minus(mUserList.size))
                        Tools.visibleViews(mRecyclerView)
                        Tools.inVisibleViews(lytNetworkError, lytHttpError, lytNoItems, type = Tools.GONE)
                    } else if (response.body()!!.results.isEmpty() && mUserList.isEmpty()) {
                        Tools.visibleViews(lytNoItems)
                    }
                } else {
                    mSwipeRefreshLayout setRefresh false
                    loadMore = false
                    if (mUserList.isEmpty()) {
                        Tools.visibleViews(lytHttpError)
                        Tools.inVisibleViews(mRecyclerView, lytNetworkError, lytNoItems, type = Tools.GONE)
                    } else snackBar = PopupUtils.showHttpErrorSnackBar(mSwipeRefreshLayout) { load() }
                }

            }

            override fun onSearchUsersFailed(call: Call<SearchUsers>, t: Throwable) {
                Log.d(TAG, t.message!!)
                mSwipeRefreshLayout setRefresh false
                loadMore = false
                if (mUserList.isEmpty()) {
                    Tools.visibleViews(lytNetworkError)
                    Tools.inVisibleViews(mRecyclerView, lytHttpError, lytNoItems, type = Tools.GONE)
                } else snackBar = PopupUtils.showNetworkErrorSnackBar(mSwipeRefreshLayout) { load() }
            }
        }

        // Adapter listener
        mOnUserClickListener = object : UserAdapter.OnUserClickListener {
            override fun onUserClick(user: User, view: View, imgUserProfile: View, pos: Int) {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, imgUserProfile, ViewCompat.getTransitionName(imgUserProfile)!!)
                val intent = Intent(activity, UserActivity::class.java)
                intent.putExtra(Preferences.USER, Gson().toJson(user))
                startActivity(intent, options.toBundle())
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true
        val view = inflater.inflate(R.layout.fragment_search_user, container, false)
        ButterKnife.bind(this, view)

        /** Recycler View */
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setItemViewCacheSize(5)
        mUserAdapter = UserAdapter(ArrayList(), context, mOnUserClickListener)
        mRecyclerView.adapter = mUserAdapter

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
            mUserList.clear()
            load()
            mUserAdapter = UserAdapter(ArrayList(), context, mOnUserClickListener)
            mRecyclerView.adapter = mUserAdapter
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
            mService.searchUsers(mQuery!!, mPage, WallBox.DEFAULT_PER_PAGE, mOnSearchUsersListener)
        } else {
            mSwipeRefreshLayout setRefresh false
        }
    }

    private fun updateAdapter(users: ArrayList<User>) {
        mUserAdapter.addAll(users)
    }

}
