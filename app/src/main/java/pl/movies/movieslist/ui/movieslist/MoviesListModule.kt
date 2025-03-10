package pl.movies.movieslist.ui.movieslist

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.movies.domain.nowplaying.MovieWithFavoriteStatus
import pl.movies.domain.nowplaying.Repository
import pl.movies.domain.paging.Pager

@Module
@InstallIn(SingletonComponent::class)
class MoviesListModule {

  @Provides
  fun provideMovieWithFavoriteStatusPager(repository: Repository): Pager<MovieWithFavoriteStatus> =
    Pager(repository::getAllMovies, repository::clearAllMoviesData)
}
