package pl.movies.movieslist.ui.movieslist

import pl.movies.domain.nowplaying.NowPlayingMovie

sealed class MoviesListState {
  data class SuccessState(
    val noMoreItemsAvailable: Boolean = false,
    val isReloading: Boolean = false,
    val movies: List<NowPlayingMovie> = emptyList()
  ) : MoviesListState()

  data class ErrorState(
    val throwable: Throwable
  ) : MoviesListState()
}
