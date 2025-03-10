package pl.movies.domain.nowplaying

import pl.movies.network.api.movies.MovieDto
import pl.movies.persistence.database.favorites.MoviesWithFavoriteStatusView
import pl.movies.persistence.database.movies.MovieEntity
import javax.inject.Inject

class MoviesMapper @Inject constructor() {

  fun mapDtoToPersistence(data: List<MovieDto>): List<MovieEntity> =
    data.map {
      MovieEntity(
        id = it.id,
        posterPath = it.posterPath ?: "",
        adult = it.adult,
        originalTitle = it.originalTitle ?: ""
      )
    }

  fun mapPersistenceToDomain(data: List<MoviesWithFavoriteStatusView>): List<MovieWithFavoriteStatus> =
    data.map {
      MovieWithFavoriteStatus(
        id = it.id,
        posterPath = it.posterPath,
        adult = it.adult,
        originalTitle = it.originalTitle,
        isFavorite = it.favoriteStatusId != 0L
      )
    }

}
