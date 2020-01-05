package com.dilipsuthar.wallbox.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.items.OpenSourceLicense
import com.dilipsuthar.wallbox.viewholders.LicenseViewHolder

class OpenSourceLicenseAdapter(
    private val listLicenses: ArrayList<OpenSourceLicense>?,
    private val listener: OnLicenseClickListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_open_source_license, parent, false)
        return LicenseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listLicenses?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val openSourceLicense = listLicenses?.get(position) ?: OpenSourceLicense()
        if (holder is LicenseViewHolder) {
            holder.bind(openSourceLicense, listener)
        }
    }

    /** Interface */
    interface OnLicenseClickListener {
        fun onLicenseClick(openSourceLicense: OpenSourceLicense, view: View)
    }

}