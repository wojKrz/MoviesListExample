package pl.movies.persistence.database.favorites

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView(
    viewName = "movies_with_favorite_status_view",
    value = """
        SELECT M.id, M.poster_path, M.adult, M.original_title, F.id AS favorite_status_id 
         FROM movies M
         LEFT JOIN favorite_movies F
            ON F.movie_id = M.id
    """
)
data class MoviesWithFavoriteStatusView(

    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "poster_path")
    var posterPath: String,

    @ColumnInfo(name = "adult")
    var adult: Boolean,

    @ColumnInfo(name = "original_title")
    var originalTitle: String,

    @ColumnInfo(name = "favorite_status_id")
    var favoriteStatusId: Long
)
