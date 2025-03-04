package pl.movies.domain.nowplaying

data class NowPlayingMovie(
  val id: Long,
  val posterPath: String,
  val adult: Boolean,
  val originalTitle: String,
  val isFavorite: Boolean
)
