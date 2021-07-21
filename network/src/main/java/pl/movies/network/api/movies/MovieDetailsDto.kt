package pl.movies.network.api.movies

import com.google.gson.annotations.SerializedName

data class MovieDetailsDto(

    @SerializedName(value = "id")
    val id: Long,

    @SerializedName(value = "original_title")
    val originalTitle: String,

    @SerializedName(value = "poster_path")
    val posterPath: String?,

    @SerializedName(value = "backdrop")
    val backdrop: String?,

    @SerializedName(value = "overview")
    val overview: String,

    @SerializedName(value = "release_date")
    var releaseDate: String
)
