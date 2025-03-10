package pl.movies.domain.paging

data class PagerData(
    val noMoreItemsAvailable: Boolean = false,
    val error: Throwable? = null
)
