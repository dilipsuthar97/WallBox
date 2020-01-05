package com.dilipsuthar.wallbox.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.WallBox
import com.dilipsuthar.wallbox.data_source.model.User
import com.dilipsuthar.wallbox.helpers.setRefresh
import com.dilipsuthar.wallbox.preferences.Preferences
import com.dilipsuthar.wallbox.utils.Tools
/**
 * Created by DILIP SUTHAR on 30/08/19
 */
class UserLikedFragment : BasePhotosFragment() {
    private val TAG = "WallBox.UserLP_Frag"

    companion object {
        fun newInstance(sort: String): UserLikedFragment {
            val fragment = UserLikedFragment()

            val args = Bundle()
            args.putString(Preferences.SORT, sort)
            fragment.arguments = args

            return fragment
        }
    }

    private lateinit var mUser: User

    /**
     * it assign user from the parent activity, --must be call
     *
     * @param user user dataModel for getting username to request collection
     */
    fun setUser(user: User) {
        mUser = user
    }

    override fun getView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_user_liked, container, false)
    }

    override fun loadPhotos(totalItem: Int) {
        if (mUser != null) {

            if (totalItem != mUser.total_likes) {
                mSwipeRefreshLayout setRefresh true
                loadMore = true
                if (snackBar != null) snackBar?.dismiss()
                mService?.requestUserLikedPhotos(mUser.username, mPage, WallBox.DEFAULT_PER_PAGE, mSort!!, this)
            }

        } else {
            Tools.visibleViews(lytHttpError)
            Tools.inVisibleViews(mRecyclerView, lytNetworkError, type = Tools.GONE)
            mSwipeRefreshLayout setRefresh false
        }
    }
}
