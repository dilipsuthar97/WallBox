package com.dilipsuthar.wallbox.data.model

data class SearchUsers(
    val total: Int,
    val total_pages: Int,
    val results: List<User>
)