package pl.movies.movieslist.ui.movieslist

sealed class MoviesListState {
    data class SuccessState(
        val noMoreItemsAvailable: Boolean = false,
        val isReloading: Boolean = false
    ) : MoviesListState()

    data class ErrorState(
        val throwable: Throwable
    ) : MoviesListState()
}
