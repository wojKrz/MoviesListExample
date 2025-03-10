package pl.movies.domain.paging

import app.cash.turbine.test
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PagerTest {

  val getNextPage = mockk<suspend (Int) -> PageMetadata>()
  val repository = mockk<MockPageResultsRepository>()
  val sut = Pager(repository::clearPageData)

  @Before
  fun before() {
    coEvery { repository.clearPageData() } just Runs
  }

  @Test
  fun `When next page is fetched successfully then data is emitted`() = runTest {
    coEvery { getNextPage(any()) } returns PageMetadata(1, 2)

    val pageResult = PagerData()
    sut.pagedData.test {
      sut.getNextPageWith(getNextPage)

      assertEquals(pageResult, awaitItem())
    }
  }

  @Test
  fun `When next page is fetched successfully then next page is fetched with incremented page number`() =
    runTest {
      coEvery { getNextPage(any()) } returns PageMetadata(1, 2)

      sut.getNextPageWith(getNextPage)
      coVerify { getNextPage(1) }

      sut.getNextPageWith(getNextPage)
      coVerify { getNextPage(2) }
    }

  @Test
  fun `When there are no more items to fetch then result indicates end of list`() = runTest {
    coEvery { getNextPage(any()) } returns PageMetadata(1, 1)

    val pageResult = PagerData(noMoreItemsAvailable = true)
    sut.pagedData.test {
      sut.getNextPageWith(getNextPage)

      assertEquals(pageResult, awaitItem())
    }
  }

  @Test
  fun `When error occurs during page fetch then result contains last data and error info`() =
    runTest {
      val error = Throwable("Paging error")
      coEvery { getNextPage(any()) } throws error

      val pageResult = PagerData(error = error)
      sut.pagedData.test {
        sut.getNextPageWith(getNextPage)

        assertEquals(pageResult, awaitItem())
      }
    }

  @Test
  fun `When error occurs during page fetch then next fetch attempt uses the same page number`() =
    runTest {
      val error = Throwable("Paging error")
      coEvery { getNextPage(any()) } throws error

      sut.getNextPageWith(getNextPage)
      coVerify { getNextPage(1) }

      sut.getNextPageWith(getNextPage)
      coVerify { getNextPage(1) }
    }

  @Test
  fun `When pager is restarted then next page is loaded with first page index`() = runTest {
    coEvery { getNextPage(any()) } returns PageMetadata(1, 2)

    sut.pagedData.test {
      sut.getNextPageWith(getNextPage)
      coVerify { getNextPage(1) }
      awaitItem()

      sut.restart()
      sut.getNextPageWith(getNextPage)
      coVerify { getNextPage(1) }
      awaitItem()
    }
  }

  @Test
  fun `When pager is restarted then page data is cleared`() = runTest {
    coEvery { getNextPage(any()) } returns PageMetadata(1, 2)

    sut.pagedData.test {
      sut.restart()
      coVerify { repository.clearPageData() }
    }
  }
}
