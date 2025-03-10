package pl.movies.domain.paging

interface MockPageResultsRepository {
  suspend fun getLatestPagedData(): List<Int>
  suspend fun clearPageData()
}
