package com.dilipsuthar.wallbox.data.model

data class Location(
    var city: String? = null,
    var country: String? = null,
    var position: Position? = null
) {
    constructor(): this("","",Position())
}

/** Position */
data class Position(
    var latitude: Long,
    var longitude: Long
) {
    constructor(): this(-1, -1)
}