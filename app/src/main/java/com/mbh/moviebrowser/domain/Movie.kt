package com.mbh.moviebrowser.domain

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("genre_ids") val genres: List<Long>,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val coverUrl: String?,
    @SerializedName("vote_average") val rating: Float,
    val isFavorite: Boolean = false,
    val genrePreview: String?,
) {
    val resolvedCoverUrl: String
        get() = "https://image.tmdb.org/t/p/w500$coverUrl"
}
