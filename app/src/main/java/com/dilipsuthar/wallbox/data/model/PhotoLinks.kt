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

    var self: String,
    var html: String,
    var download: String,
    var download_location: String
) {
    constructor() : this("", "", "", "")
}