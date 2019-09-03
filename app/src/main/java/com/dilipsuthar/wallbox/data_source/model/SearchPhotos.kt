package com.dilipsuthar.wallbox.data_source.model

data class SearchPhotos(
    val total: Int,
    val total_pages: Int,
    val results: List<Photo>
)