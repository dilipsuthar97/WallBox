package com.dilipsuthar.wallbox.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.dilipsuthar.wallbox.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.toast_layout.view.*

object Popup {

    fun showToast(context: Context?, msg: String?, duration: Int) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.toast_layout, null)
        view.text_msg.text = msg
        val toast = Toast(context)
        toast.duration = duration
        toast.view = view
        toast.show()
    }

    fun showHttpErrorSnackBar(view: View, action: () -> Unit?) {
        val snackBar = Snackbar.make(view, R.string.http_error_message, Snackbar.LENGTH_INDEFINITE)
        //Tools.setSnackBarDrawable(snackBar, ContextCompat.getDrawable(context!!, R.drawable.container_snackbar_error))
        snackBar.setAction("RETRY") { action() }.show()
    }

    fun showNetworkErrorSnackBar(view: View, action: () -> Unit?) {
        val snackBar = Snackbar.make(view, R.string.no_internet_message, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction("RETRY") { action() }.show()
    }

}