package pl.movies.movieslist.ui.movieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import pl.movies.domain.favorite.ToggleFavoriteMovieData
import pl.movies.domain.favorite.ToggleFavoriteMovieStatusUsecase
import pl.movies.domain.nowplaying.ClearMoviesListCacheUsecase
import pl.movies.domain.nowplaying.GetPageOfNowPlayingMoviesUsecase
import pl.movies.domain.nowplaying.MoviesNowPlayingPageState
import pl.movies.domain.nowplaying.NowPlayingMovie
import pl.movies.domain.paging.NewPageRequestData
import pl.movies.movieslist.util.addTo
import pl.movies.persistence.database.movies.MoviesDao
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val getPageOfNowPlayingMoviesUsecase: GetPageOfNowPlayingMoviesUsecase,
    private val clearMoviesListCacheUsecase: ClearMoviesListCacheUsecase,
    private val toggleFavoriteMovieStatusUsecase: ToggleFavoriteMovieStatusUsecase
) : ViewModel() {

    private val disposables: CompositeDisposable = CompositeDisposable()

    private val _moviesListState: MutableLiveData<MoviesListState> =
        MutableLiveData()

    val moviesListState: LiveData<MoviesListState> =
        _moviesListState

    private val _moviesListData: MutableLiveData<List<NowPlayingMovie>> =
        MutableLiveData()

    val moviesListData: LiveData<List<NowPlayingMovie>> =
        _moviesListData

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

        val state = MoviesListState.SuccessState(
            isReloading = newState.isReloading,
            noMoreItemsAvailable = newState.noMoreItemsAvailable
        )

        _moviesListState.postValue(state)
    }

    private fun handleNextPageLoadingError(throwable: Throwable) {
        _moviesListState.postValue(MoviesListState.ErrorState(throwable))
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
                _moviesListData.postValue(it)
            }
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
