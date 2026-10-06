package ru.omgtu.ryabchenko.publications.domain

interface PublicationRepository {
    suspend fun getPublications(): List<Publication>
    suspend fun getPublication(id: String): Publication
}

suspend fun PublicationRepository.getPublications(query: String?): List<Publication> =
    getPublications().filterByTitle(query)

fun List<Publication>.filterByTitle(query: String?): List<Publication> {
    val needle = query?.trim().orEmpty()
    if (needle.isEmpty()) return this
    return filter { it.mainTitle.contains(needle, ignoreCase = true) }
}
