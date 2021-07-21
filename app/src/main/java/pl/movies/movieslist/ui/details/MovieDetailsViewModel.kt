package pl.movies.movieslist.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import pl.movies.domain.details.GetMovieDetailsUsecase
import pl.movies.domain.details.MovieDetails
import pl.movies.domain.details.ObserveMovieDetailsUsecase
import pl.movies.domain.favorite.ToggleFavoriteMovieStatusUsecase
import pl.movies.movieslist.ui.common.list.ErrorResources
import pl.movies.movieslist.util.SingleLiveEvent
import pl.movies.movieslist.util.addTo
import pl.movies.movieslist.util.isNetworkException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val observeMovieDetailsUsecase: ObserveMovieDetailsUsecase,
    private val getMovieDetailsUsecase: GetMovieDetailsUsecase,
    private val favoriteMovieStatusUsecase: ToggleFavoriteMovieStatusUsecase,
    private val errorResources: ErrorResources
) : ViewModel() {

    private val disposables: CompositeDisposable = CompositeDisposable()

    private var movieId: Long = 0

    private val _progressVisible: MutableLiveData<Boolean> =
        MutableLiveData()

    val progressVisible: LiveData<Boolean> =
        _progressVisible

    private val _movieDetails: MutableLiveData<MovieDetails> =
        MutableLiveData()

    val movieDetails: LiveData<MovieDetails> =
        _movieDetails

    private val _showMainContent: MutableLiveData<Boolean> =
        MutableLiveData()

    val showMainContent: LiveData<Boolean> =
        _showMainContent

    private val _showErrorPage: MutableLiveData<Boolean> =
        MutableLiveData()

    val showErrorPage: LiveData<Boolean> =
        _showErrorPage

    private val _error: SingleLiveEvent<CharSequence> =
        SingleLiveEvent()

    val error: LiveData<CharSequence> =
        _error

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun startObservingDetails(movieId: Long) {
        this.movieId = movieId
        observeMovieDetailsUsecase
            .execute(movieId)
            .subscribe(
                ::handleObserveMovieDetails
            )
            .addTo(disposables)
    }

    private fun handleObserveMovieDetails(movieDetails: MovieDetails) {
        _showMainContent.postValue(true)
        _movieDetails.postValue(movieDetails)
    }

    fun fetchMovieDetails() {
        _showErrorPage.postValue(false)
        getMovieDetailsUsecase
            .execute(movieId)
            .doOnSubscribe { _progressVisible.postValue(true) }
            .doFinally { _progressVisible.postValue(false) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                ::handleGetMovieDetails,
                ::handleGetMovieDetailsError
            )
            .addTo(disposables)
    }

    private fun handleGetMovieDetails() {}

    private fun handleGetMovieDetailsError(throwable: Throwable) {
        when {
            _movieDetails.value == null -> {
                _showErrorPage.postValue(true)
                _showMainContent.postValue(false)
            }
            throwable.isNetworkException() -> _error.postValue(errorResources.getNetworkDataNotUpToDateText())
            else -> _error.postValue(errorResources.getGeneralErrorText())
        }
    }

    fun toggleFavoriteStatus() {
        favoriteMovieStatusUsecase
            .execute(movieId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .addTo(disposables)
    }
}
