package com.dilipsuthar.wallbox.viewholders

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.adapters.OpenSourceLicenseAdapter
import com.dilipsuthar.wallbox.items.OpenSourceLicense

class LicenseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    init {
        ButterKnife.bind(this, itemView)
    }

    @BindView(R.id.tv_license_name) lateinit var licenseName: TextView
    @BindView(R.id.tv_license_owner) lateinit var licenseOwner: TextView
    @BindView(R.id.tv_license_version) lateinit var licenseVersion: TextView
    @BindView(R.id.root_view) lateinit var rootView: LinearLayout

    fun bind(license: OpenSourceLicense, listener: OpenSourceLicenseAdapter.OnLicenseClickListener?) {
        licenseName.text = license.name
        licenseOwner.text = license.ownerName
        licenseVersion.text = license.version

        rootView.setOnClickListener { view ->
            listener?.onLicenseClick(license, view)
        }
    }
}