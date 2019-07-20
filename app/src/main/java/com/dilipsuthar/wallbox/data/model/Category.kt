package com.dilipsuthar.wallbox.data.model

data class Category (

/**
    "id": 6,
    "title": "People",
    "photo_count": 9844,
    "links": {
        "self": "https://api.unsplash.com/categories/6",
        "photos": "https://api.unsplash.com/categories/6/photos"
    }
*/

    var id: Int,
    var title: String,
    var photo_count: Int,
    var links: CategoryLinks

)