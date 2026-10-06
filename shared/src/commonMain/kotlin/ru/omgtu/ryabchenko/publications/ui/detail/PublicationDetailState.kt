package ru.omgtu.ryabchenko.publications.ui.detail

import ru.omgtu.ryabchenko.publications.domain.Publication

data class PublicationDetailState(
    val publication: Publication,
    val related: List<Publication>,
)
