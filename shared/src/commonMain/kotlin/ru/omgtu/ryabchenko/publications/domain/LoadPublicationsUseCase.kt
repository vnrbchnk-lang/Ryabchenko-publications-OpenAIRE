package ru.omgtu.ryabchenko.publications.domain

class LoadPublicationsUseCase(
    private val repository: PublicationRepository,
    private val pageSize: Int,
) {
    private var nextPage = 1
    private var canLoadMore = true

    val hasMore: Boolean get() = canLoadMore

    suspend fun firstPage(): Result<List<Publication>> {
        nextPage = 1
        canLoadMore = true
        return loadNext()
    }

    suspend fun loadNext(): Result<List<Publication>> {
        if (!canLoadMore) return Result.success(emptyList())
        val result = repository.getPublications(page = nextPage, pageSize = pageSize)
        if (result.isSuccess) {
            val page = result.getOrThrow()
            nextPage += 1
            canLoadMore = page.size == pageSize
        }
        return result
    }
}
