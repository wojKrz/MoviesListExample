package pl.movies.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.movies.persistence.database.details.MovieDetailsDao
import pl.movies.persistence.database.details.MovieDetailsEntity
import pl.movies.persistence.database.details.MovieDetailsWithFavoriteStatusView
import pl.movies.persistence.database.favorites.FavoriteMovieDao
import pl.movies.persistence.database.favorites.FavoriteMovieEntity
import pl.movies.persistence.database.favorites.MoviesWithFavoriteStatusView
import pl.movies.persistence.database.movies.MovieEntity
import pl.movies.persistence.database.movies.MoviesDao

@Database(
    version = 1,
    entities = [
        MovieEntity::class,
        FavoriteMovieEntity::class,
        MovieDetailsEntity::class
    ],
    views = [
        MoviesWithFavoriteStatusView::class,
        MovieDetailsWithFavoriteStatusView::class
    ]
)
abstract class AndroidDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao

    abstract fun favoriteMoviesDao(): FavoriteMovieDao

    abstract fun movieDetailsDao(): MovieDetailsDao
}
