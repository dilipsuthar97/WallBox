package com.dilipsuthar.wallbox.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.data.model.Photo
import com.google.android.material.button.MaterialButton

object Dialog {

    const val NETWORK_ERROR = 0
    const val HTTP_ERROR = 1

    /*fun showErrorDialog(ctx: Context?, errorType: Int?, photoList: ArrayList<Photo>?, load: () -> Unit?, loadMore: () -> Unit?) {
        val dialog = Dialog(ctx!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        when (errorType) {
            NETWORK_ERROR -> dialog.setContentView(R.layout.dialog_network_error)
            HTTP_ERROR -> dialog.setContentView(R.layout.dialog_http_error)
        }
        dialog.setCancelable(false)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = lp

        (dialog.findViewById<ImageButton>(R.id.btn_dismiss)).setOnClickListener {
            dialog.cancel()
        }

        (dialog.findViewById<MaterialButton>(R.id.btn_retry)).setOnClickListener {
            dialog.cancel()
            if (photoList?.isEmpty()!!)
                load()
            else
                loadMore()
        }

        dialog.show()
    }*/

}