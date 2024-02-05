package com.mbh.moviebrowser.features.movieList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbh.moviebrowser.domain.Movie
import com.mbh.moviebrowser.domain.MovieResponse
import com.mbh.moviebrowser.enums.TimeWindow
import com.mbh.moviebrowser.repository.ApiHelper
import com.mbh.moviebrowser.store.MovieStore
import kotlinx.coroutines.launch
import com.mbh.moviebrowser.domain.GenreResponse
import com.mbh.moviebrowser.tryCatch
import retrofit2.Call
import retrofit2.await

class MovieListViewModel(
    private val movieStore: MovieStore,
    private val onError: (Throwable) -> Unit,
) : ViewModel() {
    val movies = movieStore.movies
    private val currentTimeWindow = TimeWindow.DAY
    private val localeName: String = "en-US"

    private var currentPage: Int = 1
    private var hasMorePages: Boolean = true

    private var movieListAsync: Call<MovieResponse>? = null
    private var genreListAsync: Call<GenreResponse>? = null

    private suspend fun loadGenreList() {
        tryCatch(onError) {
            genreListAsync = ApiHelper.apiService.getGenres()

            val response = genreListAsync?.await()
            if (response?.genres?.isEmpty() == true) {
                return
            }

            movieStore.genres.value = response!!.genres
            loadMovieList()
        }
    }

    private suspend fun loadMovieList() {
        if (movieStore.genres.value.isEmpty()) {
            loadGenreList()
            return
        }

        tryCatch(onError) {
            movieListAsync?.cancel()
            movieListAsync = ApiHelper.apiService.getMovies(
                currentTimeWindow.windowName,
                currentPage,
                localeName,
            )

            val response = movieListAsync?.await()
            if (response?.result?.isEmpty() == true) {
                return
            }

            currentPage = response!!.page + 1
            hasMorePages = currentPage < response.totalPages
            movieStore.movies.value += response.result
        }
    }

    fun storeMovieForNavigation(movie: Movie) {
        movieStore.detailsId.value = movie.id
    }

    fun resolveGenreList(movie: Movie): String {
        if (movieStore.genres.value.isEmpty() || movie.genres.isEmpty()) {
            return "N/A"
        }

        val resolved: List<String?> = movie.genres
            .map { genreId ->
                movieStore.genres.value.find { it.id == genreId }?.name
            }

        return resolved.joinToString(", ")
    }

    fun onBottomReached() {
        if (!hasMorePages) {
            return
        }

        viewModelScope.launch {
            loadMovieList()
        }
    }
}
