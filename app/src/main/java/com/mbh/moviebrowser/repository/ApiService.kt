package com.mbh.moviebrowser.repository

import com.mbh.moviebrowser.domain.GenreResponse
import com.mbh.moviebrowser.domain.MovieResponse
import com.mbh.moviebrowser.enums.TimeWindow
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("trending/movie/{timeWindow}")
    fun getMovies(
        @Path(value="timeWindow") timeWindow: String = TimeWindow.DAY.windowName,
        @Query("page") page: Int = 1,
        @Query("language") language: String? = "en-US",
    ): Call<MovieResponse>

    @GET("genre/movie/list")
    fun getGenres(
        @Query("language") language: String? = "en",
    ): Call<GenreResponse>
}
