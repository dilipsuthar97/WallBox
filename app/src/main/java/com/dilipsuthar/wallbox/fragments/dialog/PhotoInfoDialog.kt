package com.dilipsuthar.wallbox.fragments.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import butterknife.BindView
import butterknife.ButterKnife
import com.dilipsuthar.wallbox.R
import com.dilipsuthar.wallbox.data.model.Photo
import org.w3c.dom.Text

class PhotoInfoDialog(photo: Photo) : DialogFragment() {

    @BindView(R.id.tv_camera_make) lateinit var tvCameraMake: TextView
    @BindView(R.id.tv_camera_model) lateinit var tvCameraModel: TextView
    @BindView(R.id.tv_focal_length) lateinit var tvFocalLength: TextView
    @BindView(R.id.tv_aperture) lateinit var tvAperture: TextView
    @BindView(R.id.tv_shutter_speed) lateinit var tvShutterSpeed: TextView
    @BindView(R.id.tv_iso) lateinit var tvIso: TextView
    @BindView(R.id.tv_dimensions) lateinit var tvDimensions: TextView

    private var mPhoto: Photo = photo

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_photo_info, null, false)
        ButterKnife.bind(this, view)
        initInfoData()

        return AlertDialog.Builder(activity!!).setView(view).create()
    }

    private fun initInfoData() {
        /*if (mPhoto != null) {
            tvCameraMake.text = if (mPhoto.exif.make == null) "--" else mPhoto.exif.make
            tvCameraModel.text = if (mPhoto.exif.model == null) "--" else mPhoto.exif.model
            tvFocalLength.text = if (mPhoto.exif.focal_length == null) "--" else mPhoto.exif.focal_length
            tvAperture.text = if (mPhoto.exif.aperture == null) "--" else mPhoto.exif.aperture
            tvShutterSpeed.text = if (mPhoto.exif.exposure_time == null) "--" else mPhoto.exif.exposure_time
            tvIso.text = if (mPhoto.exif.iso == null) "--" else mPhoto.exif.iso.toString()
            tvDimensions.text =
                if ((mPhoto.width == 0) or (mPhoto.height == 0)) "--" else "${mPhoto.width} x ${mPhoto.height}"
        }*/
    }

}