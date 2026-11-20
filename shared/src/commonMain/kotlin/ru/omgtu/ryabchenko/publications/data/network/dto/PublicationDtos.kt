package ru.omgtu.ryabchenko.publications.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class PublicationPageDto(
    val header: PageHeaderDto? = null,
    val results: List<PublicationDto> = emptyList(),
)

@Serializable
data class PageHeaderDto(
    val numFound: Long = 0,
    val page: Int = 1,
    val pageSize: Int = 0,
)

@Serializable
data class PublicationDto(
    val id: String = "",
    val mainTitle: String? = null,
    val type: String? = null,
    val authors: List<AuthorDto>? = null,
    val publicationDate: String? = null,
    val descriptions: List<String>? = null,
    val subjects: List<SubjectDto>? = null,
    val publisher: String? = null,
)

@Serializable
data class AuthorDto(
    val fullName: String? = null,
)

@Serializable
data class SubjectDto(
    val subject: SubjectValueDto? = null,
)

@Serializable
data class SubjectValueDto(
    val value: String? = null,
)
