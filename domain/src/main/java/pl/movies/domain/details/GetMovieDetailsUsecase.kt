package pl.movies.domain.details

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import pl.movies.domain.common.CompletableRxUsecase
import pl.movies.network.api.movies.MoviesApi
import pl.movies.persistence.database.details.MovieDetailsDao
import javax.inject.Inject

class GetMovieDetailsUsecase @Inject constructor(
    private val moviesApi: MoviesApi,
    private val movieDetailsDao: MovieDetailsDao,
    private val mapper: MovieDetailsMapper
) : CompletableRxUsecase<Long> {

    override fun execute(arguments: Long): Completable =
        moviesApi
            .getMovieDetails(arguments)
            .map { mapper.mapDtoToPersistence(it) }
            .flatMapCompletable { movieDetailsDao.insert(it) }
            .subscribeOn(Schedulers.io())

}
