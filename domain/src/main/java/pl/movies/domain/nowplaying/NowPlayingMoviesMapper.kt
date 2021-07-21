package pl.movies.domain.nowplaying

import pl.movies.network.api.movies.NowPlayingMovieDto
import pl.movies.persistence.database.favorites.MoviesWithFavoriteStatusView
import pl.movies.persistence.database.movies.MovieEntity
import javax.inject.Inject

class NowPlayingMoviesMapper @Inject constructor() {

    fun mapDtoToPersistence(data: List<NowPlayingMovieDto>): List<MovieEntity> =
        data.map {
            MovieEntity(
                id = it.id,
                posterPath = it.posterPath ?: "",
                adult = it.adult,
                originalTitle = it.originalTitle ?: ""
            )
        }

    fun mapPersistenceToDomain(data: List<MoviesWithFavoriteStatusView>): List<NowPlayingMovie> =
        data.map {
            NowPlayingMovie(
                id = it.id,
                posterPath = it.posterPath,
                adult = it.adult,
                originalTitle = it.originalTitle,
                isFavorite = it.favoriteStatusId != 0L
            )
        }

}
