package com.dilipsuthar.wallbox.data.model

data class PhotoLinks(
/**
    "links": {
        "self": "https://api.unsplash.com/photos/QvM7SCMFtVc",
        "html": "https://unsplash.com/photos/QvM7SCMFtVc",
        "download": "https://unsplash.com/photos/QvM7SCMFtVc/download",
        "download_location": "https://api.unsplash.com/photos/QvM7SCMFtVc/download"
    }
*/

    var self: String,
    var html: String,
    var download: String,
    var download_location: String
) {
    constructor() : this("", "", "", "")
}