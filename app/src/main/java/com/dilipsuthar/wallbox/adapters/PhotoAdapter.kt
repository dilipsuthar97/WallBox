package com.dilipsuthar.wallbox.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.data.model.Photo
import com.dilipsuthar.wallbox.utils.Tools
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

/**
 * Created by Dilip on 22/07/19
 */

class PhotoAdapter(private val mPhotoList: ArrayList<Photo>, private val context: Context?) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mPhotoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = mPhotoList[position]

        holder.bg.setBackgroundColor(Color.parseColor(photo.color))
        if (photo.description != null || photo.alt_description != null) {
            holder.textDescription.text = if (photo.description != null) photo.description else photo.alt_description
            Tools.visibleViews(holder.textDescription)
        }
        holder.textPhotoBy.text = "By, ${photo.user.first_name} ${photo.user.last_name}"

        Glide.with(context!!)
            .load(photo.urls.regular)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imagePhoto)

        /*Picasso.get()
            .load(photo.urls.regular)
            .into(holder.imagePhoto)*/
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imagePhoto: ImageView = itemView.findViewById(R.id.image_photo)
        val textDescription: TextView = itemView.findViewById(R.id.text_description)
        val textPhotoBy: TextView = itemView.findViewById(R.id.text_photo_by)
        val bg: ConstraintLayout = itemView.findViewById(R.id.root_view)



    }

}