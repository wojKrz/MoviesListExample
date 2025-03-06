package pl.movies.movieslist.ui.movieslist

sealed class MoviesListIntent {
  data class NewSearchQuery(val query: String) : MoviesListIntent()
  data object LoadNextPage : MoviesListIntent()
  data object RefreshData : MoviesListIntent()
  data class ToggleMovieFavoriteStatus(val movieId: Long) : MoviesListIntent()
  data class ShowMovieDetails(val movieId: Long): MoviesListIntent()
}
