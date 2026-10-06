package ru.omgtu.ryabchenko.publications.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import ru.omgtu.ryabchenko.publications.domain.PublicationRepository
import ru.omgtu.ryabchenko.publications.ui.navigation.Navigator
import kotlin.reflect.KClass

class PublicationListViewModelFactory(
    private val navigator: Navigator,
    private val repository: PublicationRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return PublicationListViewModel(navigator = navigator, repository = repository) as T
    }
}
