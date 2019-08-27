package com.dilipsuthar.wallbox.data.model

data class Location(
    val city: String? = null,
    val country: String? = null,
    val position: Position? = null
) {
    constructor(): this("","",Position())
}

/** Position */
data class Position(
    val latitude: Long,
    val longitude: Long
) {
    constructor(): this(-1, -1)
}