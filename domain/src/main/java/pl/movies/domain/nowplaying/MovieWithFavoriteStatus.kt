package pl.movies.domain.nowplaying

data class MovieWithFavoriteStatus(
  val id: Long,
  val posterPath: String,
  val adult: Boolean,
  val originalTitle: String,
  val isFavorite: Boolean
)
