package com.dilipsuthar.wallbox.data.model

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

    var self: String,
    var html: String,
    var photos: String,
    var likes: String,
    var portfolio: String,
    var following: String,
    var followers: String

) {
    constructor() : this("", "", "", "", "", "", "")
}