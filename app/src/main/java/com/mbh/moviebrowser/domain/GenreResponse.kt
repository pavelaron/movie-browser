package com.mbh.moviebrowser.domain

import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("genres") var genres: List<Genre>,
)