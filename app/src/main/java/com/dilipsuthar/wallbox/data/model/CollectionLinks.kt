package com.dilipsuthar.wallbox.data.model

data class CollectionLinks(

    /**
    "links": {
        "self": "https://api.unsplash.com/collections/3323575",
        "html": "https://unsplash.com/collections/3323575/candy",
        "photos": "https://api.unsplash.com/collections/3323575/photos",
        "related": "https://api.unsplash.com/collections/3323575/related"
    },
     */

    var self: String,
    var html: String,
    var photos: String,
    var related: String

) {
    constructor(): this("", "", "", "")
}