package com.dilipsuthar.wallbox.data_source

import android.content.Context
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.items.OpenSourceLicense

object DataGenerator {
    fun getLicenseData(ctx: Context): ArrayList<OpenSourceLicense> {
        val items = ArrayList<OpenSourceLicense>()
        val names = ctx.resources.getStringArray(R.array.license_names)
        val ownerNames = ctx.resources.getStringArray(R.array.license_owner_names)
        val versions = ctx.resources.getStringArray(R.array.license_versions)
        val urls = ctx.resources.getStringArray(R.array.license_urls)
        for (i in names.indices) {
            items.add(OpenSourceLicense(names[i], ownerNames[i], versions[i], urls[i]))
        }

        return items
    }
}