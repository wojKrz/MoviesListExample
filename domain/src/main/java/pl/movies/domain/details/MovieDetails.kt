package pl.movies.domain.details

data class MovieDetails(
    val id: Long,
    val originalTitle: String,
    val posterPath: String,
    val backdrop: String,
    val overview: String,
    val releaseDate: String,
    val isFavorite: Boolean
)
