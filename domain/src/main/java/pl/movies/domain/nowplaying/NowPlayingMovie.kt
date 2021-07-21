package pl.movies.domain.nowplaying

data class NowPlayingMovie (

    val id: Long,

    var posterPath: String,

    var adult: Boolean,

    var originalTitle: String,

    var isFavorite: Boolean
)
