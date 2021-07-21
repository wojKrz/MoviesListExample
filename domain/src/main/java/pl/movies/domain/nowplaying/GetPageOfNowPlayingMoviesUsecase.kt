package pl.movies.domain.nowplaying

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import pl.movies.domain.common.RxUsecase
import pl.movies.domain.paging.NewPageRequestData
import pl.movies.network.api.movies.MoviesApi
import pl.movies.network.api.movies.NowPlayingMovieDto
import pl.movies.network.api.paging.PageDto
import pl.movies.persistence.database.favorites.MoviesWithFavoriteStatusView
import pl.movies.persistence.database.movies.MoviesDao
import javax.inject.Inject

data class MoviesNowPlayingPageState(
    val noMoreItemsAvailable: Boolean = false,
    val isReloading: Boolean = false,
    val isLoading: Boolean = false
)

class GetPageOfNowPlayingMoviesUsecase @Inject constructor(
    private val moviesApi: MoviesApi,
    private val moviesDao: MoviesDao,
    private val mapper: NowPlayingMoviesMapper
) : RxUsecase<NewPageRequestData, MoviesNowPlayingPageState> {

    override fun execute(arguments: NewPageRequestData): Flowable<MoviesNowPlayingPageState> =
        Flowable.just(
            MoviesNowPlayingPageState(
                isLoading = true,
                isReloading = arguments.isReloadRequest()
            )
        )
            .flatMap {
                fetchAndPersistMovies(arguments)
                    .map { createStateFromPageResult(it, arguments) }
                    .toFlowable()
            }

    fun observeData(): Flowable<List<NowPlayingMovie>> =
        moviesDao.observeAllJoinedWithFavStatus()
            .map(mapper::mapPersistenceToDomain)

    private fun fetchAndPersistMovies(arguments: NewPageRequestData): Single<PageDto<*>> =
        selectApiByQueryState(arguments)
            .flatMap {
                val resultToPersist = mapper.mapDtoToPersistence(it.results)
                moviesDao
                    .insert(resultToPersist)
                    .toSingle { it }
            }

    private fun selectApiByQueryState(arguments: NewPageRequestData): Single<PageDto<NowPlayingMovieDto>> =
        if (arguments.hasNameQuery()) {
            moviesApi
                .findMoviesByName(arguments.nameQuery, arguments.nextPageNumber)
        } else {
            moviesApi
                .getNowPlayingMovies(arguments.nextPageNumber)
        }

    private fun createStateFromPageResult(
        pageDto: PageDto<*>,
        arguments: NewPageRequestData
    ): MoviesNowPlayingPageState {
        val noMoreItems = arguments.nextPageNumber >= pageDto.totalPages
        return MoviesNowPlayingPageState(
            noMoreItemsAvailable = noMoreItems,
            isReloading = false
        )
    }
}
