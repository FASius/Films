package com.example.films.data.films.api.response

data class FilmsListResponse(
    val docs: List<Doc>,
    val limit: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)