package ru.omgtu.ryabchenko.publications.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import ru.omgtu.ryabchenko.publications.data.network.PublicationApi
import ru.omgtu.ryabchenko.publications.data.network.toModel
import ru.omgtu.ryabchenko.publications.domain.Publication
import ru.omgtu.ryabchenko.publications.domain.PublicationRepository

class NetworkPublicationRepository(
    private val api: PublicationApi,
) : PublicationRepository {

    override suspend fun getPublications(page: Int, pageSize: Int): Result<List<Publication>> =
        withContext(Dispatchers.Default) {
            api.getPublicationPage(page = page, pageSize = pageSize).map { dto ->
                dto.results.map { it.toModel() }
            }
        }

    override suspend fun searchPublications(query: String): Result<List<Publication>> =
        withContext(Dispatchers.Default) {
            api.getPublicationPage(page = 1, pageSize = SEARCH_PAGE_SIZE, title = query).map { dto ->
                dto.results.map { it.toModel() }
            }
        }

    override suspend fun getPublication(id: String): Result<Publication> =
        withContext(Dispatchers.Default) {
            api.getPublication(id).map { it.toModel() }
        }

    override suspend fun getRelated(publication: Publication): Result<List<Publication>> =
        withContext(Dispatchers.Default) {
            val pages = publication.subjects.take(RELATED_SUBJECTS).map { subject ->
                async { api.getPublicationPage(page = 1, pageSize = RELATED_PER_SUBJECT, subject = subject) }
            }.awaitAll()
            val related = LinkedHashMap<String, Publication>()
            pages.forEach { page ->
                page.getOrNull()?.results?.forEach { dto ->
                    if (dto.id != publication.id && !related.containsKey(dto.id)) {
                        related[dto.id] = dto.toModel()
                    }
                }
            }
            Result.success(related.values.toList())
        }

    private companion object {
        const val SEARCH_PAGE_SIZE = 20
        const val RELATED_SUBJECTS = 3
        const val RELATED_PER_SUBJECT = 5
    }
}
