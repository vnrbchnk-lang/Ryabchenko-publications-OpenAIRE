package ru.omgtu.ryabchenko.publications.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.omgtu.ryabchenko.publications.domain.PublicationRepository
import ru.omgtu.ryabchenko.publications.domain.getRelated
import ru.omgtu.ryabchenko.publications.ui.navigation.Navigator
import ru.omgtu.ryabchenko.publications.ui.navigation.Route

class PublicationDetailViewModel(
    private val publicationId: String,
    private val navigator: Navigator,
    private val repository: PublicationRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<PublicationDetailState?>(null)
    val state: StateFlow<PublicationDetailState?> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val publication = repository.getPublication(publicationId)
            val related = repository.getRelated(publication)
            _state.value = PublicationDetailState(publication = publication, related = related)
        }
    }

    fun onIntent(intent: PublicationDetailIntent) {
        when (intent) {
            is PublicationDetailIntent.RelatedClicked -> navigator.addToBackStack(Route.Detail(intent.id))
        }
    }
}
