package ru.omgtu.ryabchenko.publications.data.network

import ru.omgtu.ryabchenko.publications.data.network.dto.PublicationDto
import ru.omgtu.ryabchenko.publications.domain.Publication
import ru.omgtu.ryabchenko.publications.domain.PublicationType

fun PublicationDto.toModel(): Publication = Publication(
    id = id,
    mainTitle = plainText(mainTitle),
    authors = authors.mapNotNull { cleanName(it.fullName) },
    publicationDate = publicationDate.orEmpty(),
    description = plainText(descriptions.firstOrNull()),
    subjects = subjects.mapNotNull { it.subject?.value },
    type = publicationType(type),
    publisher = publisher.orEmpty(),
)

private fun publicationType(value: String?): PublicationType =
    PublicationType.entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: PublicationType.Other

private fun plainText(value: String?): String =
    value?.replace(Regex("<[^>]*>"), " ")?.replace(Regex("\\s+"), " ")?.trim().orEmpty()

private fun cleanName(value: String?): String? {
    val name = value?.trim()?.trim { it == ',' || it.isWhitespace() }
    return name?.takeIf { it.isNotEmpty() }
}
