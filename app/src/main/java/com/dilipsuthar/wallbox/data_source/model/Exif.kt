package com.dilipsuthar.wallbox.data_source.model

data class Exif(
    val make: String? = null,
    val model: String? = null,
    val exposure_time: String? = null, // Shutter speed
    val aperture: String? = null,
    val focal_length: String? = null,
    val iso: Int? = null
) {
    constructor(): this(null,null,null,null,null,null)
}