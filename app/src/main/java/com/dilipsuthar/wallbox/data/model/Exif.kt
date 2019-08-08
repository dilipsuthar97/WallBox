package com.dilipsuthar.wallbox.data.model

data class Exif(
    var make: String,
    var model: String,
    var exposure_time: String,
    var aperture: String,
    var focal_length: String,
    var iso: Int
) {
    constructor(): this("","","","","",-1)
}