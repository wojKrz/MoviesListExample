package pl.movies.domain.paging

import pl.movies.network.api.paging.PageDto
import javax.inject.Inject

class PageDataMapper<DtoContent> @Inject constructor() {

  fun mapDtoToDomain(
    page: PageDto<DtoContent>,
  ): PageMetadata =
    PageMetadata(
      page = page.page,
      totalPages = page.totalPages
    )
}
