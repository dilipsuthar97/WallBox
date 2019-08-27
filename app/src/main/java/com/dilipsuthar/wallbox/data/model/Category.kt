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

    val id: Int,
    val title: String,
    val photo_count: Int,
    val links: CategoryLinks

)