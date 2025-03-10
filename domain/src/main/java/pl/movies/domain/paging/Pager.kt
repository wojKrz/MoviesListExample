package pl.movies.domain.paging

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class Pager<T>(
  private val getDownloadedPagedData: suspend () -> List<T>,
  private val clearData: suspend () -> Unit
) {

  private var currentPageIndex = FIRST_PAGE_INDEX

  private val _pagedData = MutableSharedFlow<PagerData<T>>()
  val pagedData: Flow<PagerData<T>> = _pagedData

  suspend fun getNextPageWith(getNextPage: suspend (Int) -> PageMetadata) {
    val page = try {
      val newPage = getNextPage(currentPageIndex)

      currentPageIndex++
      PagerData(
        result = getDownloadedPagedData(),
        noMoreItemsAvailable = currentPageIndex > newPage.totalPages
      )
    } catch (e: Throwable) {
      PagerData(
        result = getDownloadedPagedData(),
        error = e
      )
    }

    _pagedData.emit(page)
  }

  suspend fun restart() {
    currentPageIndex = FIRST_PAGE_INDEX
    clearData()
  }

  companion object {
    const val FIRST_PAGE_INDEX = 1
  }
}
