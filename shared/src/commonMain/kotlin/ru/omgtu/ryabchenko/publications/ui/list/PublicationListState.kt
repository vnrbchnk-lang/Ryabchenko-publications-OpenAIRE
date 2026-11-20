package ru.omgtu.ryabchenko.publications.ui.list

import ru.omgtu.ryabchenko.publications.domain.Publication

data class PublicationListState(
    val query: String = "",
    val items: List<Publication> = emptyList(),
)
