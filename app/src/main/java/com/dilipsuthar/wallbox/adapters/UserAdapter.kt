package com.dilipsuthar.wallbox.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.data.model.Collection
import com.dilipsuthar.wallbox.data.model.User
import com.dilipsuthar.wallbox.helpers.loadUrl
import com.mikhaellopez.circularimageview.CircularImageView

class UserAdapter (
    private var mUserList: ArrayList<User>?,
    private val context: Context?,
    private val listener: OnUserClickListener?
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUserList?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user = mUserList?.get(position)
        user?.let {
            if (holder is ViewHolder) {
                holder.imgUserProfile.loadUrl(
                    it.profile_image.large,
                    R.drawable.placeholder_profile,
                    R.drawable.placeholder_profile
                )

                holder.tvUsername.text = it.username

                var name = it.first_name
                if (it.last_name != null || it.last_name != "") name += it.last_name
                holder.tvName.text = name

                holder.rootView.setOnClickListener { view ->
                    listener?.onUserClick(it, view, holder.imgUserProfile, position)
                }
            }
        }
    }


    /** view holders */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            ButterKnife.bind(this, view)
        }

        @BindView(R.id.root_view) lateinit var rootView: View
        @BindView(R.id.img_user_profile) lateinit var imgUserProfile: CircularImageView
        @BindView(R.id.tv_username) lateinit var tvUsername: TextView
        @BindView(R.id.tv_name) lateinit var tvName: TextView

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