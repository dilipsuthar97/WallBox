package com.dilipsuthar.wallbox.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.WallBox
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.toast_layout.view.*

object PopupUtils {

    fun showToast(context: Context?, msg: String?, duration: Int = Toast.LENGTH_SHORT) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.toast_layout, null)
        view.text_msg.text = msg
        val toast = Toast(context)
        toast.duration = duration
        toast.view = view
        toast.show()
    }

    fun showHttpErrorSnackBar(view: View, action: () -> Unit? = {}): Snackbar {
        val snackbar = Snackbar.make(view, R.string.desc_http_error, Snackbar.LENGTH_LONG)
        Tools.setSnackBarDrawable(snackbar, ContextCompat.getDrawable(WallBox.getInstance(), R.drawable.container_snackbar))
        snackbar.setAction(R.string.retry) { action() }.show()
        return snackbar
    }

    fun showNetworkErrorSnackBar(view: View, action: () -> Unit? = {}): Snackbar {
        val snackbar = Snackbar.make(view, R.string.desc_network_error, Snackbar.LENGTH_LONG)
        Tools.setSnackBarDrawable(snackbar, ContextCompat.getDrawable(WallBox.getInstance(), R.drawable.container_snackbar))
        snackbar.setAction(R.string.retry) { action() }.show()
        return snackbar
    }

    fun showSnackbar(view: View, message: String, duration: Int = Snackbar.LENGTH_SHORT, action: (() -> Unit?)? = null, actionText: String? = null): Snackbar {
        val snackbar = Snackbar.make(view, message, duration)
        val snackView = snackbar.view
        val tv: TextView = snackView.findViewById(com.google.android.material.R.id.snackbar_text)   // change snackbar text color
        tv.setTextColor(ThemeUtils.getThemeAttrColor(WallBox.getInstance(), R.attr.colorPrimary))
        Tools.setSnackBarDrawable(snackbar, ContextCompat.getDrawable(WallBox.getInstance(), R.drawable.container_snackbar))
        if (action != null) snackbar.setAction(actionText) { action() }
        snackbar.show()
        return snackbar
    }

}