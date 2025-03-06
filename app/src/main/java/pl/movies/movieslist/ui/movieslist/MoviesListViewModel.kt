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
import pl.movies.domain.nowplaying.ClearMoviesListCacheUsecase
import pl.movies.domain.nowplaying.GetPageOfNowPlayingMoviesUsecase
import pl.movies.domain.nowplaying.MoviesNowPlayingPageState
import pl.movies.domain.nowplaying.NowPlayingMovie
import pl.movies.domain.paging.NewPageRequestData
import pl.movies.movieslist.ui.movieslist.MoviesListIntent.LoadNextPage
import pl.movies.movieslist.ui.movieslist.MoviesListIntent.NewSearchQuery
import pl.movies.movieslist.ui.movieslist.MoviesListIntent.RefreshData
import pl.movies.movieslist.ui.movieslist.MoviesListIntent.ShowMovieDetails
import pl.movies.movieslist.ui.movieslist.MoviesListIntent.ToggleMovieFavoriteStatus
import pl.movies.movieslist.ui.movieslist.PagingInfo.CanLoadMore
import pl.movies.movieslist.ui.movieslist.PagingInfo.NoMoreItemsAvailable
import pl.movies.movieslist.util.addTo
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
  private val getPageOfNowPlayingMoviesUsecase: GetPageOfNowPlayingMoviesUsecase,
  private val clearMoviesListCacheUsecase: ClearMoviesListCacheUsecase,
  private val toggleFavoriteMovieStatusUsecase: ToggleFavoriteMovieStatusUsecase,
) : ViewModel() {

  private val disposables: CompositeDisposable = CompositeDisposable()

  private val moviesListState = MutableStateFlow(MoviesNowPlayingPageState())
  private val moviesListData = MutableStateFlow<List<NowPlayingMovie>>(emptyList())
  private val searchState = MutableStateFlow("")
  private val errors = MutableStateFlow<Throwable?>(null)
  private val state = combine(moviesListData, moviesListState, searchState, errors)
  { listData, pageState, searchText, error ->
    val pagingInfo = when {
      error != null -> PagingInfo.Error(error)
      pageState.noMoreItemsAvailable -> NoMoreItemsAvailable
      else -> CanLoadMore
    }

    MoviesListState(
      pagingInfo = pagingInfo,
      isReloading = pageState.isReloading,
      searchText = searchText,
      movies = listData,
    )
  }

  val movies: StateFlow<MoviesListState> = state
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), MoviesListState())

  private var movieNameQuery = ""

  private var currentPage = MOVIES_FIRST_PAGE_INDEX

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
    getPageOfNowPlayingMoviesUsecase
      .execute(NewPageRequestData(currentPage, movieNameQuery))
      .doOnSubscribe {
        moviesListState.value = moviesListState.value.copy(isLoading = true)
      }
      .subscribe(
        ::handleNextPageLoadingState,
        ::handleNextPageLoadingError
      )
      .addTo(disposables)
  }

  private fun handleNextPageLoadingState(newState: MoviesNowPlayingPageState) {
    if (!newState.isLoading && !newState.isReloading) {
      currentPage++
    }

    errors.value = null
    moviesListState.value = newState
  }

  private fun handleNextPageLoadingError(throwable: Throwable) {
    viewModelScope.launch {
      errors.emit(throwable)
    }
  }

  private fun refreshData() {
    currentPage = MOVIES_FIRST_PAGE_INDEX
    clearMoviesListCacheUsecase
      .execute(Unit)
      .subscribe(::loadNextPage)
      .addTo(disposables)
  }

  fun startObservingData() {
    getPageOfNowPlayingMoviesUsecase
      .observeData()
      .subscribe {
        moviesListData.value = it
      }.addTo(disposables)
  }

  private fun searchMovies(nameQuery: String) {
    ignoreNotChangedQuery(nameQuery) {
      movieNameQuery = nameQuery
      searchState.value = movieNameQuery
      refreshData()
    }
  }

  private fun ignoreNotChangedQuery(newQuery: String, onChanged: (String) -> Unit) {
    if (movieNameQuery == newQuery)
      return

    onChanged(newQuery)
  }

  private fun toggleMovieFavoriteStatus(movieId: Long) {
    toggleFavoriteMovieStatusUsecase
      .execute(movieId)
      .subscribe()
  }

  companion object {
    const val MOVIES_FIRST_PAGE_INDEX = 1
  }
}
