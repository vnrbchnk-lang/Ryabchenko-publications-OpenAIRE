package ru.omgtu.ryabchenko.publications.ui.list

import ru.omgtu.ryabchenko.publications.domain.LoadException
import ru.omgtu.ryabchenko.publications.ui.model.PublicationCardUi

sealed interface PublicationListState {
    val query: String

    data class Loading(override val query: String) : PublicationListState

    data class Loaded(
        override val query: String,
        val items: List<PublicationCardUi>,
        val hasMore: Boolean,
        val isLoadingMore: Boolean = false,
        val loadMoreError: LoadException? = null,
    ) : PublicationListState

    data class Failed(override val query: String, val error: LoadException) : PublicationListState
}

fun PublicationListState.withQuery(value: String): PublicationListState = when (this) {
    is PublicationListState.Loading -> copy(query = value)
    is PublicationListState.Loaded -> copy(query = value)
    is PublicationListState.Failed -> copy(query = value)
}
