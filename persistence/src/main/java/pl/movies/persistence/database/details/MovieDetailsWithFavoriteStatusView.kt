package pl.movies.persistence.database.details

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView(
    viewName = "movie_details_with_favorite_status_view",
    value = """
        SELECT M.*, F.id AS favorite_status_id 
         FROM movie_details M
         LEFT JOIN favorite_movies F
            ON F.movie_id = M.id
    """
)
data class MovieDetailsWithFavoriteStatusView(

    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "original_title")
    val originalTitle: String,

    @ColumnInfo(name = "poster_path")
    val posterPath: String,

    @ColumnInfo(name = "backdrop")
    val backdrop: String,

    @ColumnInfo(name = "overview")
    val overview: String,

    @ColumnInfo(name = "release_date")
    val releaseDate: String,

    @ColumnInfo(name = "favorite_status_id")
    val favoriteStatusId: Long
)
