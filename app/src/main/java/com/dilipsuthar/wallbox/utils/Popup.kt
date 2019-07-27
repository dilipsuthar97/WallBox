package com.dilipsuthar.wallbox.utils

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.dilipsuthar.wallbox.R
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

}