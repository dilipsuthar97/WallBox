package com.dilipsuthar.wallbox.viewholders

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.adapters.CollectionAdapter
import com.dilipsuthar.wallbox.data_source.model.Collection
import com.dilipsuthar.wallbox.helpers.loadUrl

class CollectionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val imageCollection: ImageView = itemView!!.findViewById(R.id.img_collection)
    private val tvCollectionTitle: TextView = itemView!!.findViewById(R.id.tv_collection_title)
    private val tvWallpaperCount: TextView = itemView!!.findViewById(R.id.tv_wallpaper_count)

    fun bind(collection: Collection?, ctx: Context?, listener: CollectionAdapter.OnCollectionClickListener?) {
        collection?.let {
            tvCollectionTitle.text = it.title
            if (it.cover_photo != null) {
                imageCollection.loadUrl(it.cover_photo.urls.regular)
            }
            tvWallpaperCount.text = "${it.total_photos} ${ctx?.resources?.getString(R.string.wallpapers)}"

            imageCollection.setOnClickListener { view ->
                listener?.onCollectionClick(it, view, position)
            }
        }
    }
}