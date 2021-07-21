package pl.movies.network.api.paging

import com.google.gson.annotations.SerializedName

data class PageDto<Content>(

    @SerializedName("page")
    var page: Int,

    @SerializedName("total_pages")
    var totalPages: Int,

    @SerializedName("results")
    var results: List<Content>

)
