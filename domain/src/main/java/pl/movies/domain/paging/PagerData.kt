package pl.movies.domain.paging

data class PagerData<Result>(
    val result: List<Result>,
    val noMoreItemsAvailable: Boolean = false,
    val error: Throwable? = null
)
