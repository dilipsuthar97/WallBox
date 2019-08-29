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
                tvLikes: "https://api.unsplash.com/users/nate_dumlao/tvLikes",
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

    val id: String,
    val updated_at: String,
    val username: String = "",
    val name: String,
    val first_name: String = "",
    val last_name: String = "",
    val twitter_username: String,
    val portfolio_url: String = "",
    val bio: String = "",
    val location: String = "",
    val links: UserLinks,
    val profile_image: ProfileImage,
    val instagram_username: String,
    val total_collections: Int,
    val total_likes: Int,
    val total_photos: Int,
    val followers_count: Int,
    val following_count: Int,
    val downloads: Int,
    val accepted_tos: Boolean,
    val badge: Badge,
    val tags: UserTags

) {
    constructor() : this("", "", "", "", "",
        "", "", "", "", "",
        UserLinks(), ProfileImage(), "", -1, -1,
        -1, -1, -1, -1, false,
        Badge(), UserTags()
    )
}

data class UserTags(
    val custom: List<Tag> = emptyList(),
    val aggregated: List<Tag> = emptyList()
) {
    constructor(): this(emptyList(), emptyList())
}