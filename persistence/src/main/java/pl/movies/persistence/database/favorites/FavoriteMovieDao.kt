package pl.movies.persistence.database.favorites

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

@Dao
abstract class FavoriteMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addFavoriteMovie(entity: FavoriteMovieEntity): Completable

    @Query(
        value = """
            SELECT id from favorite_movies WHERE movie_id = :movieId
        """
    )
    abstract fun getFavoriteMovie(movieId: Long): Maybe<Long>

    @Query(
        value = """
        DELETE FROM favorite_movies WHERE movie_id = :movieId
    """
    )
    abstract fun removeFavoriteMovie(movieId: Long): Completable

}
