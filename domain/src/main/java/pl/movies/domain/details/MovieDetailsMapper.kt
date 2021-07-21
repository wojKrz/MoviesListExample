package pl.movies.domain.details

import pl.movies.network.api.movies.MovieDetailsDto
import pl.movies.persistence.database.details.MovieDetailsEntity
import pl.movies.persistence.database.details.MovieDetailsWithFavoriteStatusView
import javax.inject.Inject

class MovieDetailsMapper @Inject constructor() {

    fun mapPersistenceToDomain(persistence: MovieDetailsWithFavoriteStatusView) =
        MovieDetails(
            id = persistence.id,
            originalTitle = persistence.originalTitle,
            posterPath = persistence.posterPath,
            backdrop = persistence.backdrop,
            overview = persistence.overview,
            releaseDate = persistence.releaseDate,
            isFavorite = persistence.favoriteStatusId != 0L
        )

    fun mapDtoToPersistence(dto: MovieDetailsDto) =
        MovieDetailsEntity(
            id = dto.id,
            originalTitle = dto.originalTitle,
            posterPath = dto.posterPath.orEmpty(),
            backdrop = dto.backdrop.orEmpty(),
            overview = dto.overview,
            releaseDate = dto.releaseDate
        )
}
