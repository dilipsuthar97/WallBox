package com.dilipsuthar.wallbox.adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.data_source.model.Photo
import com.dilipsuthar.wallbox.viewholders.LoadingViewHolder
import com.dilipsuthar.wallbox.viewholders.PhotoViewHolder
import com.mikhaellopez.circularimageview.CircularImageView

/**
 * @adapter It bind the photos data to recycler view
 *
 * @param mPhotoList List of all photos
 * @param layoutType layout types list view & grid view
 * @param context Application context for shared preferences
 * @param activity Activity object
 * @param listener On collection item click listener when user tap
 */
class PhotoAdapter
    constructor(
        private var mPhotoList: ArrayList<Photo>?,
        private val layoutType: String,
        private val context: Context?,
        private val activity: Activity?,
        private val listener: OnItemClickListener?
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = PhotoAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?

        return if (viewType == TYPE_PHOTO) {
            view = when(layoutType) {
                "list" -> LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
                else -> LayoutInflater.from(parent.context).inflate(R.layout.item_photo_grid, parent, false)
            }
            PhotoViewHolder(view)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_loader, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return mPhotoList?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (mPhotoList?.get(position)!!.width == -1) TYPE_LOADING else TYPE_PHOTO
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val photo = mPhotoList?.get(position)

        if (holder is PhotoViewHolder) {
            holder.bind(photo, position, context, activity, listener)
        }
    }

    /** methods */
    fun addAll(photos: ArrayList<Photo>) {
        mPhotoList?.addAll(photos)
        if (mPhotoList?.size!! >= 30) {
            notifyItemInserted(mPhotoList?.size!!.minus(28))
        }
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
        fun onUserProfileClick(photo: Photo, pos: Int, imgPhotoBy: CircularImageView)
        fun onItemClick(photo: Photo, view: View, pos: Int, imageView: ImageView)
        fun onItemLongClick(photo: Photo, view: View, pos: Int, imageView: ImageView)
    }

    /** Static */
    companion object {
        const val TYPE_PHOTO = 0
        const val TYPE_LOADING = 1
    }
}