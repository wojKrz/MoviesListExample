package pl.movies.movieslist.ui.movieslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import pl.movies.domain.favorite.ToggleFavoriteMovieStatusUsecase
import pl.movies.domain.nowplaying.ClearMoviesListCacheUsecase
import pl.movies.domain.nowplaying.GetPageOfNowPlayingMoviesUsecase
import pl.movies.domain.nowplaying.MoviesNowPlayingPageState
import pl.movies.domain.nowplaying.NowPlayingMovie
import pl.movies.domain.paging.NewPageRequestData
import pl.movies.movieslist.ui.movieslist.MoviesListState.ErrorState
import pl.movies.movieslist.util.addTo
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
  private val getPageOfNowPlayingMoviesUsecase: GetPageOfNowPlayingMoviesUsecase,
  private val clearMoviesListCacheUsecase: ClearMoviesListCacheUsecase,
  private val toggleFavoriteMovieStatusUsecase: ToggleFavoriteMovieStatusUsecase
) : ViewModel() {

  private val disposables: CompositeDisposable = CompositeDisposable()

  private val moviesListState = MutableStateFlow(MoviesNowPlayingPageState())
  private val moviesListData = MutableStateFlow<List<NowPlayingMovie>>(emptyList())
  private val state = moviesListData.combine(moviesListState) { listData, pageState ->
    MoviesListState.SuccessState(
      noMoreItemsAvailable = pageState.noMoreItemsAvailable,
      isReloading = pageState.isReloading,
      movies = listData
    )
  }

  private val errors = MutableSharedFlow<ErrorState>()

  val movies: Flow<MoviesListState> = merge(state, errors)

  private var movieNameQuery = ""

  private var currentPage = MOVIES_FIRST_PAGE_INDEX

  override fun onCleared() {
    super.onCleared()
    disposables.clear()
  }

  fun loadNextPage() {
    getPageOfNowPlayingMoviesUsecase
      .execute(NewPageRequestData(currentPage, movieNameQuery))
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

    moviesListState.value = newState
  }

  private fun handleNextPageLoadingError(throwable: Throwable) {
    viewModelScope.launch {
      errors.emit(ErrorState(throwable))
    }
  }

  fun refreshData() {
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

  fun searchMovies(nameQuery: String) {
    ignoreNotChangedQuery(nameQuery) {
      movieNameQuery = nameQuery
      refreshData()
    }
  }

  private fun ignoreNotChangedQuery(newQuery: String, onChanged: (String) -> Unit) {
    if (movieNameQuery == newQuery)
      return

    onChanged(newQuery)
  }

  fun toggleMovieFavoriteStatus(movieId: Long) {
    toggleFavoriteMovieStatusUsecase
      .execute(movieId)
      .subscribe()
  }

  companion object {
    const val MOVIES_FIRST_PAGE_INDEX = 1
  }
}
