package com.dilipsuthar.wallbox.data.model

data class PhotoLinks(
/**
    "links": {
        "self": "https://api.unsplash.com/photos/QvM7SCMFtVc",
        "html": "https://unsplash.com/photos/QvM7SCMFtVc",
        "tvDownload": "https://unsplash.com/photos/QvM7SCMFtVc/tvDownload",
        "download_location": "https://api.unsplash.com/photos/QvM7SCMFtVc/tvDownload"
    }
*/

    val self: String,
    val html: String,
    val download: String,
    val download_location: String
) {
    constructor() : this("", "", "", "")
}