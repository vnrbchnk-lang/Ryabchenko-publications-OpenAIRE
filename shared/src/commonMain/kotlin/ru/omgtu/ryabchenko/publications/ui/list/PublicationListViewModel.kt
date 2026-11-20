package ru.omgtu.ryabchenko.publications.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.omgtu.ryabchenko.publications.domain.LoadException
import ru.omgtu.ryabchenko.publications.domain.LoadPublicationsUseCase
import ru.omgtu.ryabchenko.publications.domain.Publication
import ru.omgtu.ryabchenko.publications.domain.PublicationRepository
import ru.omgtu.ryabchenko.publications.ui.model.toCardUi
import ru.omgtu.ryabchenko.publications.ui.navigation.Navigator
import ru.omgtu.ryabchenko.publications.ui.navigation.Route

private const val SEARCH_DEBOUNCE_MS = 400L

class PublicationListViewModel(
    private val navigator: Navigator,
    private val repository: PublicationRepository,
    private val loadPublications: LoadPublicationsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<PublicationListState>(PublicationListState.Loading(query = ""))
    val state: StateFlow<PublicationListState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadFirstPage()
    }

    fun onIntent(intent: PublicationListIntent) {
        when (intent) {
            is PublicationListIntent.CardClicked -> navigator.addToBackStack(Route.Detail(intent.id))
            is PublicationListIntent.QueryChanged -> onQueryChanged(intent.value)
            PublicationListIntent.Retry -> retry()
            PublicationListIntent.LoadMore -> loadMore()
        }
    }

    private fun loadFirstPage() {
        _state.value = PublicationListState.Loading(query = "")
        viewModelScope.launch {
            val result = loadPublications.firstPage()
            _state.value = result.toLoadedState(query = "", hasMore = loadPublications.hasMore)
        }
    }

    private fun onQueryChanged(value: String) {
        _state.update { it.withQuery(value) }
        searchJob?.cancel()
        if (value.isBlank()) {
            loadFirstPage()
            return
        }
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS)
            _state.value = PublicationListState.Loading(query = value)
            val result = repository.searchPublications(value.trim())
            if (_state.value.query != value) return@launch
            _state.value = result.toLoadedState(query = value, hasMore = false)
        }
    }

    private fun retry() {
        val query = _state.value.query
        if (query.isBlank()) loadFirstPage() else onQueryChanged(query)
    }

    private fun loadMore() {
        val current = _state.value
        if (current !is PublicationListState.Loaded || current.isLoadingMore || !current.hasMore) return
        _state.value = current.copy(isLoadingMore = true, loadMoreError = null)
        viewModelScope.launch {
            val result = loadPublications.loadNext()
            val updated = _state.value
            if (updated !is PublicationListState.Loaded) return@launch
            result.fold(
                onSuccess = { page ->
                    _state.value = updated.copy(
                        items = updated.items + page.map { it.toCardUi() },
                        hasMore = loadPublications.hasMore,
                        isLoadingMore = false,
                    )
                },
                onFailure = { error ->
                    _state.value = updated.copy(isLoadingMore = false, loadMoreError = error.asLoadException())
                },
            )
        }
    }

    private fun Result<List<Publication>>.toLoadedState(query: String, hasMore: Boolean): PublicationListState = fold(
        onSuccess = { items ->
            PublicationListState.Loaded(query = query, items = items.map { it.toCardUi() }, hasMore = hasMore)
        },
        onFailure = { error ->
            PublicationListState.Failed(query = query, error = error.asLoadException())
        },
    )

    private fun Throwable.asLoadException(): LoadException =
        this as? LoadException ?: LoadException.BadResponse
}
