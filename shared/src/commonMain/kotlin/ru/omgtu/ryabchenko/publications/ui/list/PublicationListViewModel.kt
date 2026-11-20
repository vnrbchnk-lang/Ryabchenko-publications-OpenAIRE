package ru.omgtu.ryabchenko.publications.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.omgtu.ryabchenko.publications.domain.PublicationRepository
import ru.omgtu.ryabchenko.publications.domain.getPublications
import ru.omgtu.ryabchenko.publications.ui.navigation.Navigator
import ru.omgtu.ryabchenko.publications.ui.navigation.Route

class PublicationListViewModel(
    private val navigator: Navigator,
    private val repository: PublicationRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(PublicationListState())
    val state: StateFlow<PublicationListState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        search(query = null)
    }

    private fun search(query: String?) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val items = repository.getPublications(query)
            _state.update { it.copy(items = items) }
        }
    }

    fun onIntent(intent: PublicationListIntent) {
        when (intent) {
            is PublicationListIntent.CardClicked -> navigator.addToBackStack(Route.Detail(intent.id))

            is PublicationListIntent.QueryChanged -> {
                _state.update { it.copy(query = intent.value) }
                search(query = intent.value)
            }
        }
    }
}
