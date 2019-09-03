package com.dilipsuthar.wallbox.data_source.model

data class Badge(

    /**
    "badge": {
        "title": "Book contributor",
        "primary": true,
        "slug": "book-contributor",
        "link": "https://book.unsplash.com"
    },*/

    val title: String,
    val primary: Boolean,
    val slug: String,
    val link: String

) {
    constructor(): this("", false, "", "")
}