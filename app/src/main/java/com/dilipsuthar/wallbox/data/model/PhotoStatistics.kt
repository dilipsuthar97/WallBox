package com.dilipsuthar.wallbox.data.model

data class PhotoStatistics(

    val id: String,
    val downloads: Downloads,
    val views: Views,
    val likes: Likes

)

/** downloads data Class */
data class Downloads(
    val total: Int
)

/** tvViews data Class */
data class Views(
    val total: Int
)

/** tvLikes data Class */
data class Likes(
    val total: Int
)