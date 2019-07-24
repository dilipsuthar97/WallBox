package com.dilipsuthar.wallbox.data.model

data class User (

    /**
        user: {
            id: "dG6lZyj-wvM",
            updated_at: "2019-07-18T04:34:49-04:00",
            username: "nate_dumlao",
            name: "Nathan Dumlao",
            first_name: "Nathan",
            last_name: "Dumlao",
            twitter_username: "Nate_Dumlao",
            portfolio_url: "http://www.nathandumlaophotos.com",
            bio: "Brand Consultant and Content Creator living in Los Angeles creating up the coast.",
            location: "Los Angeles",
            links: {
                self: "https://api.unsplash.com/users/nate_dumlao",
                html: "https://unsplash.com/@nate_dumlao",
                photos: "https://api.unsplash.com/users/nate_dumlao/photos",
                likes: "https://api.unsplash.com/users/nate_dumlao/likes",
                portfolio: "https://api.unsplash.com/users/nate_dumlao/portfolio",
                following: "https://api.unsplash.com/users/nate_dumlao/following",
                followers: "https://api.unsplash.com/users/nate_dumlao/followers"
            },
            profile_image: {
                small: "https://images.unsplash.com/profile-1495427732560-fe5248ad6638?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32",
                medium: "https://images.unsplash.com/profile-1495427732560-fe5248ad6638?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64",
                large: "https://images.unsplash.com/profile-1495427732560-fe5248ad6638?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128"
            },
            instagram_username: "nate_dumlao",
            total_collections: 2,
            total_likes: 1339,
            total_photos: 1142,
            accepted_tos: true
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
    var links: UserLinks,
    var profile_image: ProfileImage,
    var instagram_username: String,
    var total_collections: Int,
    var total_likes: Int,
    var total_photos: Int,
    var accepted_tos: Boolean

) {
    constructor() : this("", "", "", "", "",
        "", "", "", "", "",
        UserLinks(), ProfileImage(), "", -1, -1,
        -1, false
        )
}