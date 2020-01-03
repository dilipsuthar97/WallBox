package com.dilipsuthar.wallbox.helpers

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.dilipsuthar.wallbox.WallBox
import java.io.File

/**
 * Singleton pattern
 */
class DownloadHelper constructor(context: Context) {

    private var mDownloadManager: DownloadManager? = null

    companion object {
        private var instance: DownloadHelper? = null

        fun getInstance(context: Context): DownloadHelper? {
            if (instance == null)
                instance = DownloadHelper(context)

            return instance as DownloadHelper
        }
    }

    init {
        mDownloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    fun addDownloadRequest(url: String, fileName: String): Long? {
        if (mDownloadManager == null) return -1

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(fileName)
            .setDestinationInExternalPublicDir(WallBox.DOWNLOAD_PATH, fileName)
            .setVisibleInDownloadsUi(true)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        request.allowScanningByMediaScanner()

        val downloadId = mDownloadManager?.enqueue(request)

        return downloadId
    }

    fun fileExists(fileName: String): Boolean {
        return File(Environment.getExternalStorageDirectory().absolutePath + WallBox.DOWNLOAD_PATH + fileName).exists()
    }

}