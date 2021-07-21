package pl.movies.domain.details

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import pl.movies.domain.common.RxUsecase
import pl.movies.persistence.database.details.MovieDetailsDao
import javax.inject.Inject

class ObserveMovieDetailsUsecase @Inject constructor(
    private val movieDetailsDao: MovieDetailsDao,
    private val mapper: MovieDetailsMapper
) : RxUsecase<Long, MovieDetails> {

    override fun execute(arguments: Long): Flowable<MovieDetails> =
        movieDetailsDao
            .observeDetailsWithFavStatus(arguments)
            .map(mapper::mapPersistenceToDomain)
            .subscribeOn(Schedulers.io())
}
