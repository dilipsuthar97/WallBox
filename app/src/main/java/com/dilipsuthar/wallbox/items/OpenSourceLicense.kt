package com.dilipsuthar.wallbox.items

data class OpenSourceLicense(
    val name: String,
    val ownerName: String,
    val version: String,
    val url: String
) {
    constructor(): this("","","","")
}