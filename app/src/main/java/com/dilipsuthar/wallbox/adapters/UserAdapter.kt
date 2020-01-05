package com.dilipsuthar.wallbox.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.data_source.model.User
import com.dilipsuthar.wallbox.helpers.loadUrl
import com.dilipsuthar.wallbox.viewholders.UserViewHolder
import com.mikhaellopez.circularimageview.CircularImageView

class UserAdapter (
    private var mUserList: ArrayList<User>?,
    private val context: Context?,
    private val listener: OnUserClickListener?
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUserList?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user = mUserList?.get(position)
        if (holder is UserViewHolder) {
            holder.bind(user, listener, position)
        }
    }

    /** @method update adapter and notify item change */
    fun addAll(users: ArrayList<User>) {
        mUserList?.addAll(users)
        notifyItemInserted(mUserList?.size!!.minus(28))
    }

    /** Interface */
    interface OnUserClickListener {
        fun onUserClick(user: User, view: View, imgUserProfile: View, pos: Int)
    }

}