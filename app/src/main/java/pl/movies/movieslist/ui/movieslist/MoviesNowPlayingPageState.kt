package pl.movies.movieslist.ui.movieslist

import pl.movies.domain.nowplaying.MovieWithFavoriteStatus

data class MoviesNowPlayingPageState(
    val items: List<MovieWithFavoriteStatus> = emptyList(),
    val error: Throwable? = null,
    val noMoreItemsAvailable: Boolean = false,
)
