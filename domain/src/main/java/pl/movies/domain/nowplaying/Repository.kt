package pl.movies.domain.nowplaying

import pl.movies.domain.paging.PageMetadata
import pl.movies.domain.paging.PageDataMapper
import pl.movies.network.api.movies.MoviesApi
import pl.movies.network.api.movies.MovieDto
import pl.movies.persistence.database.movies.MoviesDao
import javax.inject.Inject

class Repository @Inject constructor(
  private val moviesApi: MoviesApi,
  private val moviesDao: MoviesDao,
  private val mapper: MoviesMapper,
  private val pageMapper: PageDataMapper<MovieDto>
) {
  suspend fun getAllMovies(): List<MovieWithFavoriteStatus> =
    moviesDao.getAllJoinedWithFavStatus()
      .run(mapper::mapPersistenceToDomain)

  suspend fun loadNextMoviesPage(pageNo: Int): PageMetadata {
    val page = moviesApi.getNowPlayingMovies(pageNo)

    page.results
      .run(mapper::mapDtoToPersistence)
      .also { moviesDao.insert(it) }

    return page.run { pageMapper.mapDtoToDomain(this) }
  }

  suspend fun loadNextMoviesSearchPage(query: String, pageNo: Int): PageMetadata {
    val page = moviesApi.findMoviesByName(query, pageNo)
    page.results
      .run(mapper::mapDtoToPersistence)
      .also { moviesDao.insert(it) }

    return page.run { pageMapper.mapDtoToDomain(this) }
  }

  suspend fun clearAllMoviesData() = moviesDao.clearAll()
}
