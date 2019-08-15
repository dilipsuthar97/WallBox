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
                holder.tvCollectionTitle.text = it.title
                holder.imageCollection.loadUrl(it.cover_photo.urls.regular)
                holder.tvWallpaperCount.text = "${it.total_photos} Wallpapers"

                holder.imageCollection.setOnClickListener { view ->
                    listener?.onCollectionClick(it, view, position)
                }
            }
        }
    }

    /** View holders */
    class CollectionViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        val imageCollection: ImageView = itemView!!.findViewById(R.id.img_collection)
        val tvCollectionTitle: TextView = itemView!!.findViewById(R.id.tv_collection_title)
        val tvWallpaperCount: TextView = itemView!!.findViewById(R.id.tv_wallpaper_count)
    }

    /** Methods */
    fun addAll(collections: ArrayList<Collection>) {
        mCollectionList?.addAll(collections)
        notifyItemInserted(mCollectionList?.size!!.minus(28))
    }

    /** Interface */
    interface OnCollectionClickListener {
        fun onCollectionClick(collection: Collection, view: View, pos: Int)
    }

    /** Static */
    companion object {
        const val TYPE_COLLECTION = 0
    }
}