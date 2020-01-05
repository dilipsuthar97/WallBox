package com.dilipsuthar.wallbox.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.data_source.model.Collection
import com.dilipsuthar.wallbox.preferences.Preferences
import com.dilipsuthar.wallbox.helpers.loadUrl
import com.dilipsuthar.wallbox.viewholders.CollectionViewHolder

/**
 * @adapter It bind the collection data to recycler view
 *
 * @param mCollectionList List of all collections
 * @param context Application context for shared preferences
 * @param listener On collection item click listener when user tap
 */
class CollectionAdapter
    constructor(
        private var mCollectionList: ArrayList<Collection>?,
        private val context: Context?,
        private val listener: OnCollectionClickListener?
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View? = LayoutInflater.from(parent.context).inflate(R.layout.item_collection, parent, false)
        return CollectionViewHolder(view!!)
    }

    override fun getItemCount(): Int {
        return mCollectionList?.size ?: 0
    }

    /*override fun getItemViewType(position: Int): Int {
        return TYPE_COLLECTION
    }*/

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val collection = mCollectionList?.get(position)
        if (holder is CollectionViewHolder) {
            holder.bind(collection, context, listener)
        }
    }

    /** update adapter and notify item change */
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