package ru.omgtu.ryabchenko.publications.ui.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class Navigator {
    private val _navStack = MutableStateFlow<List<Route>>(listOf(Route.List))
    val navStack: StateFlow<List<Route>> = _navStack.asStateFlow()

    fun addToBackStack(route: Route) {
        _navStack.update { it + route }
    }

    fun back() {
        _navStack.update { currentStack ->
            if (currentStack.size > 1) {
                currentStack.dropLast(1)
            } else {
                currentStack
            }
        }
    }
}
