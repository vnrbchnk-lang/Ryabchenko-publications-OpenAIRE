package ru.omgtu.ryabchenko.publications.ui.detail

import ru.omgtu.ryabchenko.publications.domain.LoadException
import ru.omgtu.ryabchenko.publications.ui.model.PublicationDetailUi

sealed interface PublicationDetailState {
    data object Loading : PublicationDetailState
    data class Loaded(val details: PublicationDetailUi) : PublicationDetailState
    data object NotFound : PublicationDetailState
    data class Failed(val error: LoadException) : PublicationDetailState
}
