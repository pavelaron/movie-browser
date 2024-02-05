package com.mbh.moviebrowser.features.movieDetails

import androidx.lifecycle.ViewModel
import com.mbh.moviebrowser.store.MovieStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class MovieDetailsViewModel(
    private val movieStore: MovieStore,
) : ViewModel() {

    val movie = movieStore.movies.map { it.firstOrNull { it.id == movieStore.detailsId.value } }

    fun onFavoriteClicked(isFavorite: Boolean) {
        movieStore.movies.update { movies ->
            movies.map {
                when (it.id == movieStore.detailsId.value) {
                    true -> it.copy(isFavorite = isFavorite)
                    false -> it
                }
            }
        }
    }
}
