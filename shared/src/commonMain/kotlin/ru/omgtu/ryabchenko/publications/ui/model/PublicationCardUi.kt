package ru.omgtu.ryabchenko.publications.ui.model

import ru.omgtu.ryabchenko.publications.domain.Publication

data class PublicationCardUi(
    val id: String,
    val mainTitle: String,
    val authors: String,
    val publicationDate: String,
)

fun Publication.toCardUi(): PublicationCardUi = PublicationCardUi(
    id = id,
    mainTitle = mainTitle,
    authors = authors.joinToString(", "),
    publicationDate = publicationDate,
)
