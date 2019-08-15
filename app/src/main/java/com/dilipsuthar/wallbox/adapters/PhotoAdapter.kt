package com.dilipsuthar.wallbox.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.data.model.Photo
import com.dilipsuthar.wallbox.utils.loadUrl
import com.mikhaellopez.circularimageview.CircularImageView

/**
 * Created by DILIP SUTHAR on 22/07/19
 */

class PhotoAdapter
    constructor(
        private var mPhotoList: ArrayList<Photo>?,
        private val layoutType: String,
        private val listener: OnItemClickListener?
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?

        return if (viewType == TYPE_PHOTO) {
            view = if (layoutType == "list")
                LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
            else
                LayoutInflater.from(parent.context).inflate(R.layout.item_photo_grid, parent, false)
            PhotoViewHolder(view)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_loader, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return if (mPhotoList == null) 0 else mPhotoList?.size!!
    }

    override fun getItemViewType(position: Int): Int {
        return if (mPhotoList?.get(position)!!.width == -1) TYPE_LOADING else TYPE_PHOTO
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val photo = mPhotoList?.get(position)

        if (holder is PhotoViewHolder) {

            photo?.let {

                val txtPhotoBy = photo.user.first_name
                if (it.user.last_name != null) txtPhotoBy + " ${it.user.last_name}"

                holder.rootView.setBackgroundColor(Color.parseColor(it.color))
                holder.textPhotoBy.text = "By, $txtPhotoBy"
                holder.textLikes.text = "${it.likes} Likes"
                holder.btnDownload.setOnClickListener {
                    // TODO: Download image from here
                }

                holder.imagePhoto.loadUrl(it.urls.regular)

                holder.imageUserProfile.loadUrl(
                    it.user.profile_image.large,
                    R.drawable.placeholder_profile,
                    R.drawable.placeholder_profile)

                holder.rootView.setOnLongClickListener { view ->
                    listener?.onItemLongClick(it, view!!, position, holder.imagePhoto)
                    true
                }

                holder.rootView.setOnClickListener { view ->
                    listener?.onItemClick(it, view, position, holder.imagePhoto)
                }
            }

        }
    }

    /** view holders */
    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            ButterKnife.bind(this, itemView)
        }

        @BindView(R.id.image_photo) lateinit var imagePhoto: ImageView
        @BindView(R.id.image_user_profile) lateinit var imageUserProfile: CircularImageView
        @BindView(R.id.text_photo_by) lateinit var textPhotoBy: TextView
        @BindView(R.id.text_likes) lateinit var textLikes: TextView
        @BindView(R.id.root_view) lateinit var rootView: View
        @BindView(R.id.btn_download) lateinit var btnDownload: ImageButton
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /** methods */
    fun addAll(photos: ArrayList<Photo>) {
        mPhotoList?.addAll(photos)
        notifyItemInserted(mPhotoList?.size!!.minus(28))
    }

    fun addFooter() {
        mPhotoList?.add(Photo())
        notifyItemInserted(mPhotoList?.size!!.minus(1))
    }

    fun removeFooter() {
        if (mPhotoList?.size!! >= 1) {
            mPhotoList?.removeAt(mPhotoList?.size!!.minus(1))
        }
    }

    /** interface */
    interface OnItemClickListener {
        fun onItemClick(photo: Photo, view: View, pos: Int, imageView: ImageView)
        fun onItemLongClick(photo: Photo, view: View, pos: Int, imageView: ImageView)
    }

    /** Static */
    companion object {
        const val TYPE_PHOTO = 0
        const val TYPE_LOADING = 1
    }
}