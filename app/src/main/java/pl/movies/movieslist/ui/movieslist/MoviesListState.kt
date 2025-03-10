package pl.movies.movieslist.ui.movieslist

import pl.movies.domain.nowplaying.MovieWithFavoriteStatus
import pl.movies.movieslist.ui.movieslist.PagingInfo.CanLoadMore

data class MoviesListState(
  val searchText: String = "",
  val pagingInfo: PagingInfo = CanLoadMore,
  val movies: List<MovieWithFavoriteStatus> = emptyList(),
)

sealed class PagingInfo {
  data object CanLoadMore : PagingInfo()
  data object NoMoreItemsAvailable : PagingInfo()
  data class Error(val throwable: Throwable): PagingInfo()
}
