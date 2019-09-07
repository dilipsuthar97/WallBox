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

class OpenSourceLicenseAdapter(
    private val listLicenses: ArrayList<OpenSourceLicense>?,
    private val listener: OnLicenseClickListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_open_source_license, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listLicenses?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val openSourceLicense = listLicenses?.get(position) ?: OpenSourceLicense()
        if (holder is ViewHolder) {
            holder.licenseName.text = openSourceLicense.name
            holder.licenseOwner.text = openSourceLicense.ownerName
            holder.licenseVersion.text = openSourceLicense.version

            holder.rootView.setOnClickListener { view ->
                listener?.onLicenseClick(openSourceLicense, view)
            }
        }
    }

    /** View holder */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            ButterKnife.bind(this, view)
        }

        @BindView(R.id.tv_license_name) lateinit var licenseName: TextView
        @BindView(R.id.tv_license_owner) lateinit var licenseOwner: TextView
        @BindView(R.id.tv_license_version) lateinit var licenseVersion: TextView
        @BindView(R.id.root_view) lateinit var rootView: LinearLayout

    }

    /** Interface */
    interface OnLicenseClickListener {
        fun onLicenseClick(openSourceLicense: OpenSourceLicense, view: View)
    }

}