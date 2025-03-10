package pl.movies.domain.paging

interface MockPageResultsRepository {
  suspend fun clearPageData()
}
