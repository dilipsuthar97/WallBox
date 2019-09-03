package com.dilipsuthar.wallbox.data_source.model

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
            tvLikes: "https://api.unsplash.com/users/squareinc/tvLikes",
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

    val id: String,
    val updated_at: String,
    val username: String,
    val name: String,
    val first_name: String,
    val last_name: String,
    val twitter_username: String,
    val portfolio_url: String,
    val bio: String,
    val location: String,
    val links: SponsoredLinks,
    val profile_image: ProfileImage,
    val instagram_username: String,
    val total_collections: Int,
    val total_likes: Int,
    val total_photos: Int,
    val accepted_tos: Boolean

) {
    constructor() : this("", "", "", "", "",
        "", "", "", "", "",
        SponsoredLinks(), ProfileImage(), "", -1, -1,
        -1, false
    )
}