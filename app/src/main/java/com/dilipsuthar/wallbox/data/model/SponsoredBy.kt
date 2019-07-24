package com.dilipsuthar.wallbox.data.model

data class SponsoredBy (

    /**
    sponsored_by: {
        id: "ezqM0zHZ8Z8",
        updated_at: "2019-07-18T02:42:31-04:00",
        username: "squareinc",
        name: "Square",
        first_name: "Square",
        last_name: null,
        twitter_username: "Square",
        portfolio_url: "https://squareup.com/",
        bio: "Building simple tools to help people participate and thrive in the economy.",
        location: "San Francisco, CA",
        links: {
            self: "https://api.unsplash.com/users/squareinc",
            html: "https://unsplash.com/@squareinc",
            photos: "https://api.unsplash.com/users/squareinc/photos",
            likes: "https://api.unsplash.com/users/squareinc/likes",
            portfolio: "https://api.unsplash.com/users/squareinc/portfolio",
            following: "https://api.unsplash.com/users/squareinc/following",
            followers: "https://api.unsplash.com/users/squareinc/followers"
        },
        profile_image: {
            small: "https://images.unsplash.com/profile-1532031424191-d9fffab22d78?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32",
            medium: "https://images.unsplash.com/profile-1532031424191-d9fffab22d78?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64",
            large: "https://images.unsplash.com/profile-1532031424191-d9fffab22d78?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128"
        },
        instagram_username: "Square",
        total_collections: 4,
        total_likes: 44,
        total_photos: 0,
        accepted_tos: false
    }
     */

    var id: String,
    var updated_at: String,
    var username: String,
    var name: String,
    var first_name: String,
    var last_name: String,
    var twitter_username: String,
    var portfolio_url: String,
    var bio: String,
    var location: String,
    var links: SponsoredLinks,
    var profile_image: ProfileImage,
    var instagram_username: String,
    var total_collections: Int,
    var total_likes: Int,
    var total_photos: Int,
    var accepted_tos: Boolean

) {
    constructor() : this("", "", "", "", "",
        "", "", "", "", "",
        SponsoredLinks(), ProfileImage(), "", -1, -1,
        -1, false
    )
}