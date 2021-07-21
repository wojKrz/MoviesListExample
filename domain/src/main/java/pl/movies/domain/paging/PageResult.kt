package pl.movies.domain.paging

data class PageResult<Result>(
    val result: List<Result>
)