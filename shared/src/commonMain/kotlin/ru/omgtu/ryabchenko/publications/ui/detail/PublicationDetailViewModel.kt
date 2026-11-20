package ru.omgtu.ryabchenko.publications.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.omgtu.ryabchenko.publications.domain.LoadException
import ru.omgtu.ryabchenko.publications.domain.PublicationRepository
import ru.omgtu.ryabchenko.publications.ui.model.toDetailUi
import ru.omgtu.ryabchenko.publications.ui.navigation.Navigator
import ru.omgtu.ryabchenko.publications.ui.navigation.Route

class PublicationDetailViewModel(
    private val publicationId: String,
    private val navigator: Navigator,
    private val repository: PublicationRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<PublicationDetailState>(PublicationDetailState.Loading)
    val state: StateFlow<PublicationDetailState> = _state.asStateFlow()

    init {
        load()
    }

    fun onIntent(intent: PublicationDetailIntent) {
        when (intent) {
            is PublicationDetailIntent.RelatedClicked -> navigator.addToBackStack(Route.Detail(intent.id))
            PublicationDetailIntent.Retry -> load()
        }
    }

    private fun load() {
        _state.value = PublicationDetailState.Loading
        viewModelScope.launch {
            val result = repository.getPublication(publicationId)
            _state.value = result.fold(
                onSuccess = { publication ->
                    val related = repository.getRelated(publication).getOrDefault(emptyList())
                    PublicationDetailState.Loaded(publication.toDetailUi(related))
                },
                onFailure = { error ->
                    when (val e = error.asLoadException()) {
                        LoadException.NotFound -> PublicationDetailState.NotFound
                        else -> PublicationDetailState.Failed(e)
                    }
                },
            )
        }
    }

    private fun Throwable.asLoadException(): LoadException =
        this as? LoadException ?: LoadException.BadResponse
}
