package pl.movies.persistence.database.movies

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import pl.movies.persistence.database.favorites.MoviesWithFavoriteStatusView

@Dao
abstract class MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(entities: List<MovieEntity>): Completable

    @Query(
        value = """
            SELECT * FROM movies_with_favorite_status_view
        """
    )
    abstract fun observeAllJoinedWithFavStatus(): Flowable<List<MoviesWithFavoriteStatusView>>

    @Transaction
    @Query(
        value = """
        DELETE FROM movies
    """
    )
    abstract fun clearAll(): Completable

}
