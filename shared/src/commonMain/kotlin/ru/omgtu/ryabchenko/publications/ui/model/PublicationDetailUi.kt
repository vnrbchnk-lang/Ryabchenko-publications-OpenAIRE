package ru.omgtu.ryabchenko.publications.ui.model

import ru.omgtu.ryabchenko.publications.domain.Publication
import ru.omgtu.ryabchenko.publications.domain.PublicationType

data class PublicationDetailUi(
    val mainTitle: String,
    val authors: String,
    val publicationDate: String,
    val publisher: String,
    val type: PublicationType,
    val subjects: String,
    val description: String,
    val related: List<PublicationCardUi>,
)

fun Publication.toDetailUi(related: List<Publication>): PublicationDetailUi = PublicationDetailUi(
    mainTitle = mainTitle,
    authors = authors.joinToString(", "),
    publicationDate = publicationDate,
    publisher = publisher,
    type = type,
    subjects = subjects.joinToString(", "),
    description = description,
    related = related.map { it.toCardUi() },
)
