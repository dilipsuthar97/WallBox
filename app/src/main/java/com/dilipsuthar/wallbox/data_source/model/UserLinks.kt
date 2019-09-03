package com.dilipsuthar.wallbox.data_source.model

data class UserLinks (
    /**
        links: {
            self: "https://api.unsplash.com/users/nate_dumlao",
            html: "https://unsplash.com/@nate_dumlao",
            photos: "https://api.unsplash.com/users/nate_dumlao/photos",
            tvLikes: "https://api.unsplash.com/users/nate_dumlao/tvLikes",
            portfolio: "https://api.unsplash.com/users/nate_dumlao/portfolio",
            following: "https://api.unsplash.com/users/nate_dumlao/following",
            followers: "https://api.unsplash.com/users/nate_dumlao/followers"
        }
    */

    val self: String,
    val html: String,
    val photos: String,
    val likes: String,
    val portfolio: String,
    val following: String,
    val followers: String

) {
    constructor() : this("", "", "", "", "", "", "")
}