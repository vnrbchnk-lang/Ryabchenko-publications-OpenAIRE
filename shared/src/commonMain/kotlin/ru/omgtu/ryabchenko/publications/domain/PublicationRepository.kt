package ru.omgtu.ryabchenko.publications.domain

interface PublicationRepository {
    suspend fun getPublications(page: Int, pageSize: Int): Result<List<Publication>>
    suspend fun searchPublications(query: String): Result<List<Publication>>
    suspend fun getPublication(id: String): Result<Publication>
    suspend fun getRelated(publication: Publication): Result<List<Publication>>
}
