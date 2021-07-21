package pl.movies.persistence.database.movies

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movies",
    indices = [Index(unique = true, value = ["id"])]
)
data class MovieEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "key_id")
    val keyId: Long = 0,

    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "poster_path")
    var posterPath: String,

    @ColumnInfo(name = "adult")
    var adult: Boolean,

    @ColumnInfo(name = "original_title")
    var originalTitle: String
)
