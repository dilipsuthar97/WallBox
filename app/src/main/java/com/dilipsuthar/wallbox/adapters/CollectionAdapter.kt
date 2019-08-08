package com.dilipsuthar.wallbox.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.data.model.Collection
import com.dilipsuthar.wallbox.utils.loadUrl

class CollectionAdapter
    constructor(
        private var mCollectionList: ArrayList<Collection>?,
        private val context: Context?,
        private val listener: OnCollectionClickListener?
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View? = LayoutInflater.from(parent.context).inflate(R.layout.item_collection, parent, false)
        return CollectionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (mCollectionList == null) 0 else mCollectionList?.size!!
    }

    /*override fun getItemViewType(position: Int): Int {
        return TYPE_COLLECTION
    }*/

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val collection = mCollectionList?.get(position)
        if (holder is CollectionViewHolder) {
            collection?.let {
                holder.txtCollectionTitle.text = it.title
                holder.imagePhoto.loadUrl(it.cover_photo.urls.regular)
                holder.txtWallpaperCount.text = "${it.total_photos} Wallpapers"

                holder.imagePhoto.setOnClickListener { view ->
                    listener?.onCollectionClick(it, view, position, holder.imagePhoto)
                }
            }
        }
    }

    /** View holders */
    class CollectionViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        val imagePhoto: ImageView = itemView!!.findViewById(R.id.image_photo)
        val txtCollectionTitle: TextView = itemView!!.findViewById(R.id.txt_collection_title)
        val txtWallpaperCount: TextView = itemView!!.findViewById(R.id.txt_wallpaper_count)
    }

    /** Methods */
    fun addAll(collections: ArrayList<Collection>) {
        mCollectionList?.addAll(collections)
        notifyItemInserted(mCollectionList?.size!!.minus(28))
    }

    /** Interface */
    interface OnCollectionClickListener {
        fun onCollectionClick(collection: Collection, view: View, pos: Int, imageView: ImageView)
    }

    /** Static */
    companion object {
        const val TYPE_COLLECTION = 0
    }
}