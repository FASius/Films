package com.example.films.data.films.api

import com.example.films.data.films.api.response.FilmDetailsResponse
import com.example.films.data.films.api.response.FilmsListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmsApi {

    @GET("/v1.3/movie")
    suspend fun getFilms(
        @Query("name") query: String?,
        @Query("sortField") filterBy: String,
        @Query("sortType") filterType: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("selectFields") selectedFields: String = "poster.previewUrl name id year rating",
        @Query("type") type: String = "movie"
    ): FilmsListResponse

    @GET("/v1.3/movie/{id}")
    suspend fun getFilmDetails(@Path("id") filmId: Long): FilmDetailsResponse

}