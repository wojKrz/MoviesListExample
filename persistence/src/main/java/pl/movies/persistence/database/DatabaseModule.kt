package pl.movies.persistence.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.movies.persistence.database.details.MovieDetailsDao
import pl.movies.persistence.database.favorites.FavoriteMovieDao
import pl.movies.persistence.database.movies.MoviesDao
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
class DatabaseModule {

    @[Provides Singleton]
    internal fun provideMoviesDao(database: AndroidDatabase): MoviesDao =
        database.moviesDao()

    @[Provides Singleton]
    internal fun provideFavoriteMoviesDao(database: AndroidDatabase): FavoriteMovieDao =
        database.favoriteMoviesDao()

    @[Provides Singleton]
    internal fun provideMovieDetailsDao(database: AndroidDatabase): MovieDetailsDao =
        database.movieDetailsDao()

    @[Provides Singleton]
    internal fun provideDatabase(@ApplicationContext context: Context): AndroidDatabase =
        Room.databaseBuilder(context, AndroidDatabase::class.java, "movies_list.db")
            .fallbackToDestructiveMigration()
            .build()
}
