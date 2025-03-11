package pl.movies.movieslist.ui.movieslist

import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.movies.domain.nowplaying.MovieWithFavoriteStatus
import pl.movies.movieslist.R
import pl.movies.movieslist.ui.movieslist.PagingInfo.CanLoadMore
import pl.movies.movieslist.ui.movieslist.PagingInfo.Error
import pl.movies.movieslist.ui.movieslist.PagingInfo.NoMoreItemsAvailable
import java.net.UnknownHostException

@Composable
fun Screen(
  viewModel: MoviesListViewModel = viewModel()
) {
  val state: MoviesListState by viewModel.movies.collectAsStateWithLifecycle()

  MoviesSearchLayout(state, viewModel::handleIntent)
}

@Composable
fun MoviesSearchLayout(
  state: MoviesListState,
  onIntent: (MoviesListIntent) -> Unit
) {
  Column {
    SearchBar(
      state.searchText
    ) { searchValue -> onIntent.invoke(MoviesListIntent.NewSearchQuery(searchValue)) }
    val listState = rememberLazyGridState()

    LaunchedEffect(listState.canScrollForward) {
      if (listState.canScrollForward.not()) {
        onIntent.invoke(MoviesListIntent.LoadNextPage)
      }
    }

    LazyVerticalGrid(
      state = listState,
      columns = GridCells.Fixed(2)
    ) {
      items(state.movies) { movie ->
        MovieItem(movie, onIntent)
      }
      item(
        span = { GridItemSpan(2) }
      ) {
        when (state.pagingInfo) {
          is Error -> ErrorItem(state.pagingInfo.throwable, onIntent)
          NoMoreItemsAvailable -> NoMorePagesItem { onIntent.invoke(MoviesListIntent.RefreshData) }
          CanLoadMore -> LoadMoreItem()
        }
      }
    }
  }
}

@Composable
fun ErrorItem(throwable: Throwable, onIntent: (MoviesListIntent) -> Unit) {
  when (throwable) {
    is UnknownHostException -> NetworkErrorItem { onIntent.invoke(MoviesListIntent.LoadNextPage) }
    else -> PagingErrorItem { onIntent.invoke(MoviesListIntent.RefreshData) }
  }
}

@Composable
fun NoMorePagesItem(onIntent: (MoviesListIntent) -> Unit) {
  ReloadListItem(
    stringResource(R.string.no_more_pages),
    stringResource(R.string.no_more_pages_reload)
  ) { onIntent.invoke(MoviesListIntent.RefreshData) }
}

@Composable
fun PagingErrorItem(onIntent: (MoviesListIntent) -> Unit) {
  ReloadListItem(
    stringResource(R.string.common_error),
    stringResource(R.string.paging_error_try_again)
  ) { onIntent.invoke(MoviesListIntent.LoadNextPage) }
}

@Composable
fun NetworkErrorItem(onIntent: (MoviesListIntent) -> Unit) {
  ReloadListItem(
    stringResource(R.string.network_error),
    stringResource(R.string.paging_error_try_again)
  ) { onIntent.invoke(MoviesListIntent.RefreshData) }
}


@Composable
fun SearchBar(searchText: String, onNewSearchValue: (String) -> Unit) {
  TextField(
    value = searchText,
    onValueChange = onNewSearchValue,
    modifier = Modifier.fillMaxWidth(),
    label = { Text(stringResource(R.string.search)) },
    leadingIcon = {
      Icon(
        painter = painterResource(R.drawable.ic_baseline_search_24),
        contentDescription = null
      )
    },
  )
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun Preview_Screen() {
  MoviesSearchLayout(
    MoviesListState(
      searchText = "Lorem",
      pagingInfo = CanLoadMore,
      movies = mockMovies
    ),
  ) {}
}

val mockMovies = listOf(
  MovieWithFavoriteStatus(
    id = 1,
    posterPath = "/path",
    adult = false,
    originalTitle = "Title1",
    isFavorite = false
  ),
  MovieWithFavoriteStatus(
    id = 2,
    posterPath = "/path",
    adult = true,
    originalTitle = "Title2",
    isFavorite = false
  ),
  MovieWithFavoriteStatus(
    id = 3,
    posterPath = "/path",
    adult = false,
    originalTitle = "Title3",
    isFavorite = true
  ),
  MovieWithFavoriteStatus(
    id = 4,
    posterPath = "/path",
    adult = false,
    originalTitle = "Title4",
    isFavorite = false
  ),
  MovieWithFavoriteStatus(
    id = 5,
    posterPath = "/path",
    adult = false,
    originalTitle = "Title5",
    isFavorite = false
  ),
)
