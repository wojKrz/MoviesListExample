package pl.movies.movieslist.ui.movieslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.movies.domain.favorite.ToggleFavoriteMovieStatusUsecase
import pl.movies.domain.nowplaying.MovieWithFavoriteStatus
import pl.movies.domain.nowplaying.Repository
import pl.movies.domain.paging.PageMetadata
import pl.movies.domain.paging.Pager
import pl.movies.movieslist.ui.movieslist.MoviesListIntent.LoadNextPage
import pl.movies.movieslist.ui.movieslist.MoviesListIntent.NewSearchQuery
import pl.movies.movieslist.ui.movieslist.MoviesListIntent.RefreshData
import pl.movies.movieslist.ui.movieslist.MoviesListIntent.ShowMovieDetails
import pl.movies.movieslist.ui.movieslist.MoviesListIntent.ToggleMovieFavoriteStatus
import pl.movies.movieslist.ui.movieslist.PagingInfo.CanLoadMore
import pl.movies.movieslist.ui.movieslist.PagingInfo.NoMoreItemsAvailable
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
  private val repository: Repository,
  private val toggleFavoriteMovieStatusUsecase: ToggleFavoriteMovieStatusUsecase,
  private val pager: Pager<MovieWithFavoriteStatus>
) : ViewModel() {

  private val disposables: CompositeDisposable = CompositeDisposable()

  private val moviesListState = MutableStateFlow(MoviesNowPlayingPageState())
  private val searchState = MutableStateFlow("")
  private val state = combine(moviesListState, searchState)
  { pageState, searchText ->
    val pagingInfo = when {
      pageState.error != null -> PagingInfo.Error(pageState.error)
      pageState.noMoreItemsAvailable -> NoMoreItemsAvailable
      else -> CanLoadMore
    }

    MoviesListState(
      pagingInfo = pagingInfo,
      searchText = searchText,
      movies = pageState.items,
    )
  }

  val movies: StateFlow<MoviesListState> = state
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), MoviesListState())

  override fun onCleared() {
    super.onCleared()
    disposables.clear()
  }

  fun handleIntent(intent: MoviesListIntent) {
    when (intent) {
      is NewSearchQuery -> searchMovies(intent.query)
      is ToggleMovieFavoriteStatus -> toggleMovieFavoriteStatus(intent.movieId)
      LoadNextPage -> loadNextPage()
      RefreshData -> refreshData()
      is ShowMovieDetails -> EventHandler.triggerEvent(EventHandler.Event.ShowMovieDetails(intent.movieId))
    }
  }

  private fun loadNextPage() {
    val nextPageLoader: suspend (Int) -> PageMetadata =
      if (searchState.value.isEmpty()) {
        { page: Int -> repository.loadNextMoviesPage(page) }
      } else {
        { page: Int -> repository.loadNextMoviesSearchPage(searchState.value, page) }
      }
    viewModelScope.launch {
      pager.getNextPageWith(nextPageLoader)
    }
  }

  private fun refreshData() {
    viewModelScope.launch {
      pager.restart()
      loadNextPage()
    }
  }

  fun startObservingData() {
    viewModelScope.launch {
      pager.pagedData
        .collect {
          moviesListState.value = MoviesNowPlayingPageState(
            items = it.result,
            error = it.error,
            noMoreItemsAvailable = it.noMoreItemsAvailable
          )
        }
    }
  }

  private fun searchMovies(nameQuery: String) {
      searchState.value = nameQuery
      refreshData()
  }

  private fun toggleMovieFavoriteStatus(movieId: Long) {
    toggleFavoriteMovieStatusUsecase
      .execute(movieId)
      .subscribe()
  }
}
