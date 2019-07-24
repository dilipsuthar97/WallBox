package com.dilipsuthar.wallbox.fragments


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import butterknife.BindView
import butterknife.ButterKnife

import com.dilipsuthar.wallbox.R
import com.google.android.material.button.MaterialButton

/**
 * A simple [Fragment] subclass.
 *
 */
class CuratedWallFragment : Fragment() {

    // VIEWS
    @BindView(R.id.button) lateinit var button: MaterialButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_curated_wall, container, false)
        ButterKnife.bind(this, view)

        button.setOnClickListener {
            val dialog = Dialog(context!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_photo_viewer)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)
            dialog.show()
        }

        return view
    }

}
