package com.dilipsuthar.wallbox.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.dilipsuthar.wallbox.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.toast_layout.view.*

object PopupUtils {

    fun showToast(context: Context?, msg: String?, duration: Int) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.toast_layout, null)
        view.text_msg.text = msg
        val toast = Toast(context)
        toast.duration = duration
        toast.view = view
        toast.show()
    }

    fun showHttpErrorSnackBar(view: View, action: () -> Unit? = {}): Snackbar {
        val snackBar = Snackbar.make(view, R.string.desc_http_error, Snackbar.LENGTH_LONG)
        //Tools.setSnackBarDrawable(snackBar, ContextCompat.getDrawable(context!!, R.drawable.container_snackbar_error))
        snackBar.setAction(R.string.retry) { action() }.show()
        return snackBar
    }

    fun showNetworkErrorSnackBar(view: View, action: () -> Unit? = {}): Snackbar {
        val snackBar = Snackbar.make(view, R.string.desc_network_error, Snackbar.LENGTH_LONG)
        snackBar.setAction(R.string.retry) { action() }.show()
        return snackBar
    }

}