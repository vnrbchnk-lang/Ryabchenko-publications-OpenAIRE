package ru.omgtu.ryabchenko.publications.ui.detail

sealed interface PublicationDetailIntent {
    data class RelatedClicked(val id: String) : PublicationDetailIntent
    data object Retry : PublicationDetailIntent
}
