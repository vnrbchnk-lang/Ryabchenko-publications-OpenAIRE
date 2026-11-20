package ru.omgtu.ryabchenko.publications.ui.navigation

sealed interface Route {
    data object List : Route
    data class Detail(val id: String) : Route
}
