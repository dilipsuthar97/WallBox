package com.dilipsuthar.wallbox.data.model

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.items.AbstractItem

data class Photo (

    /**{
        id: "aH8tRjQG4XM",
        created_at: "2019-07-18T06:20:04-04:00",
        updated_at: "2019-07-18T06:55:09-04:00",
        width: 2254,
        height: 2817,
        color: "#C68E7A",
        description: null,
        alt_description: null,
        urls: {
            raw: "https://images.unsplash.com/photo-1563445192071-fb5b2fa4ad62?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjY5NjYzfQ",
            full: "https://images.unsplash.com/photo-1563445192071-fb5b2fa4ad62?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjY5NjYzfQ",
            regular: "https://images.unsplash.com/photo-1563445192071-fb5b2fa4ad62?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjY5NjYzfQ",
            small: "https://images.unsplash.com/photo-1563445192071-fb5b2fa4ad62?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjY5NjYzfQ",
            thumb: "https://images.unsplash.com/photo-1563445192071-fb5b2fa4ad62?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjY5NjYzfQ"
        },
        links: {
            self: "https://api.unsplash.com/photos/aH8tRjQG4XM",
            html: "https://unsplash.com/photos/aH8tRjQG4XM",
            download: "https://unsplash.com/photos/aH8tRjQG4XM/download",
            download_location: "https://api.unsplash.com/photos/aH8tRjQG4XM/download"
        },
        categories: [ ],
        sponsored: false,
        sponsored_by: null,
        sponsored_impressions_id: null,
        likes: 29,
        liked_by_user: false,
        current_user_collections: [ ],
        user: {
            id: "hHQGJB9ZejE",
            updated_at: "2019-07-18T07:57:50-04:00",
            username: "rotaalternativa",
            name: "Rota Alternativa",
            first_name: "Rota",
            last_name: "Alternativa",
            twitter_username: null,
            portfolio_url: "https://www.instagram.com/rotaalternativarv/",
            bio: "We are exploring the nomad and simple life the road has to offer. Living in our 1992 Fiat Talento motorhome.",
            location: null,
            links: {
                self: "https://api.unsplash.com/users/rotaalternativa",
                html: "https://unsplash.com/@rotaalternativa",
                photos: "https://api.unsplash.com/users/rotaalternativa/photos",
                likes: "https://api.unsplash.com/users/rotaalternativa/likes",
                portfolio: "https://api.unsplash.com/users/rotaalternativa/portfolio",
                following: "https://api.unsplash.com/users/rotaalternativa/following",
                followers: "https://api.unsplash.com/users/rotaalternativa/followers"
            },
            profile_image: {
                small: "https://images.unsplash.com/profile-1550700203074-81551f41d6fe?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=32&w=32",
                medium: "https://images.unsplash.com/profile-1550700203074-81551f41d6fe?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=64&w=64",
                large: "https://images.unsplash.com/profile-1550700203074-81551f41d6fe?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128"
            },
            instagram_username: "rotaalternativarv",
            total_collections: 0,
            total_likes: 1,
            total_photos: 72,
            accepted_tos: true
        }
    }*/

    var id: String,
    var created_at: String,
    var updated_at: String,
    var width: Int,
    var height: Int,
    var color: String,
    var description: String,
    var alt_description: String,
    var urls: Urls,
    var links: PhotoLinks,
    var categories: List<Category>,
    var sponsored: Boolean,
    var sponsored_by: SponsoredBy,
    var sponsored_impressions_id: String,
    var likes: Int,
    var liked_by_user: Boolean,
    //var current_user_collections: ?
    var user: User

)