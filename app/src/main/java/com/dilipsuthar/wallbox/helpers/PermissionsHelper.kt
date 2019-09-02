package com.dilipsuthar.wallbox.helpers

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object PermissionsHelper {
    private const val REQUEST_CODE = 201

    fun permissionGranted(context: Context, permissions: Array<String>): Boolean {
        return permissions.all { permission ->
            ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestPermission(activity: Activity, permissions: Array<String>) {
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE)
    }

}