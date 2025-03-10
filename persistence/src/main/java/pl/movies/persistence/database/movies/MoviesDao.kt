package pl.movies.persistence.database.movies

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import pl.movies.persistence.database.favorites.MoviesWithFavoriteStatusView

@Dao
abstract class MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entities: List<MovieEntity>)

    @Query(
        value = """
            SELECT * FROM movies_with_favorite_status_view
        """
    )
    abstract fun observeAllJoinedWithFavStatus(): Flow<List<MoviesWithFavoriteStatusView>>

    @Transaction
    @Query(
        value = """
        DELETE FROM movies
    """
    )
    abstract suspend fun clearAll()

}
