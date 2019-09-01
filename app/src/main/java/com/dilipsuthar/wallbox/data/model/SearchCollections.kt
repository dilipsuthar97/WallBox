package com.dilipsuthar.wallbox.data.model

data class SearchCollections(
    val total: Int,
    val total_pages: Int,
    val results: List<Collection>
)