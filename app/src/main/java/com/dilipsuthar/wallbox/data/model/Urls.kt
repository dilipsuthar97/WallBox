package com.dilipsuthar.wallbox.data.model

data class Urls(
/**
    "urls": {
        "raw": "https://images.unsplash.com/photo-1556742419-3d4480213f85?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjY5NjYzfQ",
        "full": "https://images.unsplash.com/photo-1556742419-3d4480213f85?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjY5NjYzfQ",
        "regular": "https://images.unsplash.com/photo-1556742419-3d4480213f85?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjY5NjYzfQ",
        "small": "https://images.unsplash.com/photo-1556742419-3d4480213f85?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjY5NjYzfQ",
        "thumb": "https://images.unsplash.com/photo-1556742419-3d4480213f85?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjY5NjYzfQ"
    },
*/

    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
) {
    constructor() : this("", "", "", "", "")
}