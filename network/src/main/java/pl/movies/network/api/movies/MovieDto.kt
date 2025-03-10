package pl.movies.network.api.movies

import com.google.gson.annotations.SerializedName

data class MovieDto(

    @SerializedName("id")
    var id: Long,

    @SerializedName("poster_path")
    var posterPath: String?,

    @SerializedName("adult")
    var adult: Boolean,

    @SerializedName("original_title")
    var originalTitle: String?
)
