package ru.omgtu.ryabchenko.publications.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import ru.omgtu.ryabchenko.publications.domain.PublicationRepository
import kotlin.reflect.KClass

class PublicationDetailViewModelFactory(
    private val repository: PublicationRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        val publicationId = checkNotNull(extras[PublicationIdKey])
        @Suppress("UNCHECKED_CAST")
        return PublicationDetailViewModel(
            publicationId = publicationId,
            repository = repository,
        ) as T
    }

    companion object {
        val PublicationIdKey = CreationExtras.Key<String>()

        fun extrasFor(publicationId: String): CreationExtras =
            MutableCreationExtras().apply { set(PublicationIdKey, publicationId) }
    }
}
