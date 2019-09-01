package com.dilipsuthar.wallbox.data.model

data class SearchPhotos(
    val total: Int,
    val total_pages: Int,
    val results: List<Photo>
)