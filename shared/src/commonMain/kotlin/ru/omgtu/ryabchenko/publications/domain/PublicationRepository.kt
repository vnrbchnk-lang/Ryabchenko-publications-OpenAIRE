package ru.omgtu.ryabchenko.publications.domain

interface PublicationRepository {
    suspend fun getPublications(): List<Publication>
    suspend fun getPublication(id: String): Publication
}
