package ru.omgtu.ryabchenko.publications.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.omgtu.ryabchenko.publications.domain.Publication
import ru.omgtu.ryabchenko.publications.domain.PublicationRepository

class PublicationDetailViewModel(
    private val publicationId: String,
    private val repository: PublicationRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<Publication?>(null)
    val state: StateFlow<Publication?> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = repository.getPublication(publicationId)
        }
    }
}
