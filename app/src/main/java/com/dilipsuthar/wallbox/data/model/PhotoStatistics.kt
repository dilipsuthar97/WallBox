package com.dilipsuthar.wallbox.data.model

data class PhotoStatistics(

    var id: String,
    var downloads: Downloads,
    var views: Views,
    var likes: Likes

)

/** downloads data Class */
data class Downloads(
    var total: Int
)

/** tvViews data Class */
data class Views(
    var total: Int
)

/** tvLikes data Class */
data class Likes(
    var total: Int
)