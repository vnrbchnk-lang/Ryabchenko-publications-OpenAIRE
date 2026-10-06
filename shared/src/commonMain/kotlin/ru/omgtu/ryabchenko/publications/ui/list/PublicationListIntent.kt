package ru.omgtu.ryabchenko.publications.ui.list

sealed interface PublicationListIntent {
    data class CardClicked(val id: String) : PublicationListIntent
}
