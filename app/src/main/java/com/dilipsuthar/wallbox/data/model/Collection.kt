package com.dilipsuthar.wallbox.data.model

data class Collection(

    /**
    {
        id: 3323575,
        title: "Candy",
        tvDescription: null,
        published_at: "2019-07-26T11:54:41-04:00",
        updated_at: "2019-07-26T11:54:41-04:00",
        curated: false,
        featured: true,
        total_photos: 41,
        private: false,
        share_key: "f73221a5391c41f06c4b65059402b3bf",
        tags: [
            {
                title: "candy"
            },
            {
                title: "sweet"
            },
            {
                title: "sugar"
            },
            {
                title: "food"
            },
            {
                title: "colorful"
            },
            {
                title: "treat"
            }
        ],
        links: {
            self: "https://api.unsplash.com/collections/3323575",
            html: "https://unsplash.com/collections/3323575/candy",
            photos: "https://api.unsplash.com/collections/3323575/photos",
            related: "https://api.unsplash.com/collections/3323575/related"
        },
        user: {
            id: "Ae-950m0rz0",
            updated_at: "2019-08-07T22:41:10-04:00",
            username: "mintyfrills",
            name: "Jennifer Carlsson",
            first_name: "Jennifer",
            last_name: "Carlsson",
            twitter_username: "IroMinto",
            portfolio_url: null,
            bio: null,
            location: "Stockholm",
            links: {
                self: "https://api.unsplash.com/users/mintyfrills",
                html: "https://unsplash.com/@mintyfrills",
                photos: "https://api.unsplash.com/users/mintyfrills/photos",
                tvLikes: "https://api.unsplash.com/users/mintyfrills/tvLikes",
                portfolio: "https://api.unsplash.com/users/mintyfrills/portfolio",
                following: "https://api.unsplash.com/users/mintyfrills/following",
                followers: "https://api.unsplash.com/users/mintyfrills/followers"
            },
            profile_image: {
                small: "https://images.unsplash.com/profile-1557772587909-0cb16fa138e2?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32",
                medium: "https://images.unsplash.com/profile-1557772587909-0cb16fa138e2?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64",
                large: "https://images.unsplash.com/profile-1557772587909-0cb16fa138e2?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128"
            },
            instagram_username: "mintoiro",
            total_collections: 68,
            total_likes: 242,
            total_photos: 0,
            accepted_tos: false
        },
        cover_photo: {
            id: "dGr6Cwp9qLs",
            created_at: "2017-07-04T15:08:07-04:00",
            updated_at: "2019-08-07T01:02:25-04:00",
            width: 3300,
            height: 2550,
            color: "#13C9FD",
            tvDescription: null,
            alt_description: null,
            urls: {
                raw: "https://images.unsplash.com/photo-1499195231111-6009d0b2f5c9?ixlib=rb-1.2.1",
                full: "https://images.unsplash.com/photo-1499195231111-6009d0b2f5c9?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb",
                regular: "https://images.unsplash.com/photo-1499195231111-6009d0b2f5c9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max",
                small: "https://images.unsplash.com/photo-1499195231111-6009d0b2f5c9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max",
                thumb: "https://images.unsplash.com/photo-1499195231111-6009d0b2f5c9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max"
            },
            links: {
                self: "https://api.unsplash.com/photos/dGr6Cwp9qLs",
                html: "https://unsplash.com/photos/dGr6Cwp9qLs",
                tvDownload: "https://unsplash.com/photos/dGr6Cwp9qLs/tvDownload",
                download_location: "https://api.unsplash.com/photos/dGr6Cwp9qLs/tvDownload"
            },
            categories: [ ],
            tvLikes: 116,
            liked_by_user: false,
            current_user_collections: [ ],
            user: {
                id: "Jv4xnOFuxoY",
                updated_at: "2019-08-05T00:24:41-04:00",
                username: "sylvanusurban",
                name: "Sylvanus Urban",
                first_name: "Sylvanus",
                last_name: "Urban",
                twitter_username: "MrSylvanusUrban",
                portfolio_url: "http://www.sylvanus-urban.com",
                bio: null,
                location: "Toronto",
                links: {
                self: "https://api.unsplash.com/users/sylvanusurban",
                html: "https://unsplash.com/@sylvanusurban",
                photos: "https://api.unsplash.com/users/sylvanusurban/photos",
                tvLikes: "https://api.unsplash.com/users/sylvanusurban/tvLikes",
                portfolio: "https://api.unsplash.com/users/sylvanusurban/portfolio",
                following: "https://api.unsplash.com/users/sylvanusurban/following",
                followers: "https://api.unsplash.com/users/sylvanusurban/followers"
            },
            profile_image: {
                small: "https://images.unsplash.com/profile-1498161027642-7d7c96e8172c?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32",
                medium: "https://images.unsplash.com/profile-1498161027642-7d7c96e8172c?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64",
                large: "https://images.unsplash.com/profile-1498161027642-7d7c96e8172c?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128"
            },
            instagram_username: "mrsylvanusurban",
            total_collections: 0,
            total_likes: 8,
            total_photos: 4,
            accepted_tos: false
        }
        },
        preview_photos: [
            {
                id: "dGr6Cwp9qLs",
                urls: {
                    raw: "https://images.unsplash.com/photo-1499195231111-6009d0b2f5c9?ixlib=rb-1.2.1",
                    full: "https://images.unsplash.com/photo-1499195231111-6009d0b2f5c9?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb",
                    regular: "https://images.unsplash.com/photo-1499195231111-6009d0b2f5c9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max",
                    small: "https://images.unsplash.com/photo-1499195231111-6009d0b2f5c9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max",
                    thumb: "https://images.unsplash.com/photo-1499195231111-6009d0b2f5c9?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max"
                }
            },
            {
            id: "JkRfhwjIU8g",
                urls: {
                    raw: "https://images.unsplash.com/photo-1529635323539-1e8597bf672d?ixlib=rb-1.2.1",
                    full: "https://images.unsplash.com/photo-1529635323539-1e8597bf672d?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb",
                    regular: "https://images.unsplash.com/photo-1529635323539-1e8597bf672d?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max",
                    small: "https://images.unsplash.com/photo-1529635323539-1e8597bf672d?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max",
                    thumb: "https://images.unsplash.com/photo-1529635323539-1e8597bf672d?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max"
                }
            },
            {
            id: "AHegElpiVj4",
                urls: {
                    raw: "https://images.unsplash.com/photo-1534118650230-cec0aa21f255?ixlib=rb-1.2.1",
                    full: "https://images.unsplash.com/photo-1534118650230-cec0aa21f255?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb",
                    regular: "https://images.unsplash.com/photo-1534118650230-cec0aa21f255?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max",
                    small: "https://images.unsplash.com/photo-1534118650230-cec0aa21f255?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max",
                    thumb: "https://images.unsplash.com/photo-1534118650230-cec0aa21f255?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max"
                }
            },
            {
                id: "NCpjzKiFte8",
                urls: {
                    raw: "https://images.unsplash.com/photo-1532457898782-a150f536d7c6?ixlib=rb-1.2.1",
                    full: "https://images.unsplash.com/photo-1532457898782-a150f536d7c6?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb",
                    regular: "https://images.unsplash.com/photo-1532457898782-a150f536d7c6?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max",
                    small: "https://images.unsplash.com/photo-1532457898782-a150f536d7c6?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max",
                    thumb: "https://images.unsplash.com/photo-1532457898782-a150f536d7c6?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max"
                }
            }
        ]
    },
     */

    val id: Int,
    val title: String,
    val description: String,
    val published_at: String,
    val updated_at: String,
    val curated: Boolean,
    val featured: Boolean,
    val total_photos: Int,
    val private: Boolean,
    val share_key: String,
    val tags: List<Tag>,
    val links: CollectionLinks,
    val user: User,
    val cover_photo: Photo,
    val preview_photos: List<PreviewPhoto>

)