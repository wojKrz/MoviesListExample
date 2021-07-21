package pl.movies.domain.favorite

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import pl.movies.domain.common.CompletableRxUsecase
import pl.movies.persistence.database.favorites.FavoriteMovieDao
import pl.movies.persistence.database.favorites.FavoriteMovieEntity
import javax.inject.Inject

typealias ToggleFavoriteMovieData = Long

class ToggleFavoriteMovieStatusUsecase @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao
) : CompletableRxUsecase<ToggleFavoriteMovieData> {

    override fun execute(arguments: ToggleFavoriteMovieData): Completable {
        return favoriteMovieDao
            .getFavoriteMovie(arguments)
            .isEmpty
            .map { it.not() }
            .concatMapCompletable { favoriteExists ->
                if (favoriteExists) {
                    favoriteMovieDao.removeFavoriteMovie(arguments)
                } else {
                    val entity = FavoriteMovieEntity(movieId = arguments)
                    favoriteMovieDao.addFavoriteMovie(entity)
                }
            }
            .subscribeOn(Schedulers.io())
    }
}
