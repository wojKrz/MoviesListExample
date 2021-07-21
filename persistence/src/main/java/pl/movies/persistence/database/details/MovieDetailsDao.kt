package pl.movies.persistence.database.details

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
abstract class MovieDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(entity: MovieDetailsEntity): Completable

    @Query(
        value = """
            SELECT * FROM movie_details_with_favorite_status_view WHERE id = :id
        """
    )
    abstract fun observeDetailsWithFavStatus(id: Long): Flowable<MovieDetailsWithFavoriteStatusView>

}
