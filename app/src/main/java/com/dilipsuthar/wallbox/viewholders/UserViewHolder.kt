package com.dilipsuthar.wallbox.viewholders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.adapters.UserAdapter
import com.dilipsuthar.wallbox.data_source.model.User
import com.dilipsuthar.wallbox.helpers.loadUrl
import com.mikhaellopez.circularimageview.CircularImageView

class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    init {
        ButterKnife.bind(this, itemView)
    }

    @BindView(R.id.root_view) lateinit var rootView: ConstraintLayout
    @BindView(R.id.img_user_profile) lateinit var imgUserProfile: CircularImageView
    @BindView(R.id.tv_username) lateinit var tvUsername: TextView
    @BindView(R.id.tv_name) lateinit var tvName: TextView

    fun bind(user: User?, listener: UserAdapter.OnUserClickListener?, position: Int) {
        user?.let {
            imgUserProfile.loadUrl(
                it.profile_image.medium,
                R.drawable.placeholder_profile,
                R.drawable.placeholder_profile
            )

            tvUsername.text = it.username

            var name = it.first_name
            name = if (it.last_name != "") "$name ${it.last_name}" else ""
            tvName.text = name

            rootView.setOnClickListener { view ->
                listener?.onUserClick(it, view, imgUserProfile, position)
            }
        }
    }
}