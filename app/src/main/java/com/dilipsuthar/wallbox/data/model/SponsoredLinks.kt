package com.dilipsuthar.wallbox.data.model

data class SponsoredLinks (
/**
    links: {
        self: "https://api.unsplash.com/users/squareinc",
        html: "https://unsplash.com/@squareinc",
        photos: "https://api.unsplash.com/users/squareinc/photos",
        tvLikes: "https://api.unsplash.com/users/squareinc/tvLikes",
        portfolio: "https://api.unsplash.com/users/squareinc/portfolio",
        following: "https://api.unsplash.com/users/squareinc/following",
        followers: "https://api.unsplash.com/users/squareinc/followers"
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
    constructor() : this ("", "", "", "", "", "", "")
}