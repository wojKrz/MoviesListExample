package pl.movies.domain.nowplaying

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import pl.movies.domain.common.CompletableRxUsecase
import pl.movies.persistence.database.movies.MoviesDao
import javax.inject.Inject

class ClearMoviesListCacheUsecase @Inject constructor(
    private val moviesDao: MoviesDao
) : CompletableRxUsecase<Unit> {

    override fun execute(arguments: Unit): Completable =
        moviesDao
            .clearAll()
            .subscribeOn(Schedulers.io())
}
