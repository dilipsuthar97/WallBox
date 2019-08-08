package com.dilipsuthar.wallbox.data.model

data class Location(
    var city: String,
    var country: String,
    var position: Position
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