package pl.movies.persistence.database.movies

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import pl.movies.persistence.database.favorites.MoviesWithFavoriteStatusView

@Dao
abstract class MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entities: List<MovieEntity>): Unit

    @Query(
        value = """
            SELECT * FROM movies_with_favorite_status_view
        """
    )
    abstract fun observeAllJoinedWithFavStatus(): Flowable<List<MoviesWithFavoriteStatusView>>

    @Query(
        value = """
            SELECT * FROM movies_with_favorite_status_view
        """
    )
    abstract suspend fun getAllJoinedWithFavStatus(): List<MoviesWithFavoriteStatusView>

    @Transaction
    @Query(
        value = """
        DELETE FROM movies
    """
    )
    abstract suspend fun clearAll()

}
