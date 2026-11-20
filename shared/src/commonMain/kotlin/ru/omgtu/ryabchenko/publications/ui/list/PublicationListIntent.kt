package ru.omgtu.ryabchenko.publications.ui.list

sealed interface PublicationListIntent {
    data class CardClicked(val id: String) : PublicationListIntent
    data class QueryChanged(val value: String) : PublicationListIntent
    data object Retry : PublicationListIntent
    data object LoadMore : PublicationListIntent
}
