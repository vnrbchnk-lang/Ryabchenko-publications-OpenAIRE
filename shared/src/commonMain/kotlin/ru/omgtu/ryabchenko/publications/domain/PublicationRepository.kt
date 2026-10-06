package ru.omgtu.ryabchenko.publications.domain

interface PublicationRepository {
    suspend fun getPublications(): List<Publication>
    suspend fun getPublication(id: String): Publication
}

suspend fun PublicationRepository.getPublications(query: String?): List<Publication> =
    getPublications().filterByTitle(query)

suspend fun PublicationRepository.getRelated(publication: Publication): List<Publication> =
    getPublications().filter { other ->
        other.id != publication.id && other.subjects.any { it in publication.subjects }
    }

fun List<Publication>.filterByTitle(query: String?): List<Publication> {
    val needle = query?.trim().orEmpty()
    if (needle.isEmpty()) return this
    return filter { it.mainTitle.contains(needle, ignoreCase = true) }
}
