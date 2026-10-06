package ru.omgtu.ryabchenko.publications.data

import ru.omgtu.ryabchenko.publications.domain.Publication
import ru.omgtu.ryabchenko.publications.domain.PublicationRepository

class PublicationRepositoryImpl : PublicationRepository {
    override suspend fun getPublications(): List<Publication> = mockPublications

    override suspend fun getPublication(id: String): Publication = mockPublications.first { it.id == id }
}
