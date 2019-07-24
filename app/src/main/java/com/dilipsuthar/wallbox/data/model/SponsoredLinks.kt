package com.dilipsuthar.wallbox.data.model

data class SponsoredLinks (
/**
    links: {
        self: "https://api.unsplash.com/users/squareinc",
        html: "https://unsplash.com/@squareinc",
        photos: "https://api.unsplash.com/users/squareinc/photos",
        likes: "https://api.unsplash.com/users/squareinc/likes",
        portfolio: "https://api.unsplash.com/users/squareinc/portfolio",
        following: "https://api.unsplash.com/users/squareinc/following",
        followers: "https://api.unsplash.com/users/squareinc/followers"
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
    constructor() : this ("", "", "", "", "", "", "")
}