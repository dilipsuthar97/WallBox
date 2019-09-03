package com.dilipsuthar.wallbox.data_source.model

data class CollectionLinks(

    /**
    "links": {
        "self": "https://api.unsplash.com/collections/3323575",
        "html": "https://unsplash.com/collections/3323575/candy",
        "photos": "https://api.unsplash.com/collections/3323575/photos",
        "related": "https://api.unsplash.com/collections/3323575/related"
    },
     */

    val self: String,
    val html: String,
    val photos: String,
    val related: String

) {
    constructor(): this("", "", "", "")
}