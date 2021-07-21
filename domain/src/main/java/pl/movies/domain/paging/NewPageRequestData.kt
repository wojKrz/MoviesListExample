package pl.movies.domain.paging

data class NewPageRequestData(
    val nextPageNumber: Int,
    val nameQuery: String = ""
) {

    fun isReloadRequest(): Boolean = nextPageNumber == FIRST_PAGE_NUMBER

    fun hasNameQuery(): Boolean = nameQuery.isNotEmpty() && nameQuery.isNotBlank()

    companion object {

        private const val FIRST_PAGE_NUMBER = 1

        fun reloadRequest(): NewPageRequestData = NewPageRequestData(
            FIRST_PAGE_NUMBER
        )
    }
}
