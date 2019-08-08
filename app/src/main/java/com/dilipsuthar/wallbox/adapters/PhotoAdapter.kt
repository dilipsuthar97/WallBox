package com.dilipsuthar.wallbox.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.data.model.Photo
import com.dilipsuthar.wallbox.utils.Tools
import com.dilipsuthar.wallbox.utils.loadUrl
import com.google.android.material.card.MaterialCardView
import com.mikhaellopez.circularimageview.CircularImageView
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import java.lang.NullPointerException

/**
 * Created by Dilip on 22/07/19
 */

class PhotoAdapter
    constructor(
        private var mPhotoList: ArrayList<Photo>?,
        private var ctx: Context?,
        private var listener: OnItemClickListener?
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?

        return if (viewType == TYPE_PHOTO) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
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

            photo?.let { photo ->

                val txtPhotoBy = photo.user.first_name
                if (photo.user.last_name != null) txtPhotoBy + " ${photo.user.last_name}"

                holder.rootView.setBackgroundColor(Color.parseColor(photo.color))
                holder.textPhotoBy.text = "By, $txtPhotoBy"
                holder.textLikes.text = "${photo.likes}"
                holder.btnDownload.setOnClickListener {

                }

                /*Picasso.get()
                    .load(photo.urls.regular)
                    .into(holder.mImagePhoto)*/
                holder.imagePhoto.loadUrl(photo.urls.regular)

                /*Picasso.get()
                    .load(photo.user.profile_image.medium)
                    .placeholder(R.drawable.placeholder_profile)
                    .error(R.drawable.placeholder_profile)
                    .into(holder.imageUserProfile)*/

                holder.imageUserProfile.loadUrl(
                    photo.user.profile_image.medium,
                    R.drawable.placeholder_profile,
                    R.drawable.placeholder_profile)

                holder.rootView.setOnLongClickListener { view ->
                    listener?.onItemLongClick(photo, view!!, position, holder.imagePhoto)
                    true
                }

                holder.rootView.setOnClickListener { view ->
                    listener?.onItemClick(photo, view, position, holder.imagePhoto)
                }
            }

        }
    }

    /** view holders */
    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imagePhoto: ImageView = itemView.findViewById(R.id.image_photo)
        var imageUserProfile: CircularImageView = itemView.findViewById(R.id.image_user_profile)
        val textPhotoBy: TextView = itemView.findViewById(R.id.text_photo_by)
        val textLikes: TextView = itemView.findViewById(R.id.text_likes)
        val rootView: ConstraintLayout = itemView.findViewById(R.id.root_view)
        val btnDownload: ImageButton = itemView.findViewById(R.id.btn_download)
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressLoadMore = itemView.findViewById<ProgressBar>(R.id.progress_load_more)
    }

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