package ru.omgtu.ryabchenko.publications

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import publicationscmp.shared.generated.resources.Res
import publicationscmp.shared.generated.resources.app_title
import publicationscmp.shared.generated.resources.back
import publicationscmp.shared.generated.resources.theme_toggle
import ru.omgtu.ryabchenko.publications.data.PublicationRepositoryImpl
import ru.omgtu.ryabchenko.publications.domain.PublicationRepository
import ru.omgtu.ryabchenko.publications.ui.detail.PublicationDetailViewModelFactory
import ru.omgtu.ryabchenko.publications.ui.list.PublicationListViewModelFactory
import ru.omgtu.ryabchenko.publications.ui.navigation.AppNavDisplay
import ru.omgtu.ryabchenko.publications.ui.navigation.Navigator
import ru.omgtu.ryabchenko.publications.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var darkTheme by remember { mutableStateOf(false) }
    val repository: PublicationRepository = remember { PublicationRepositoryImpl() }
    val navigator = remember { Navigator() }
    val listViewModelFactory = remember { PublicationListViewModelFactory(navigator, repository) }
    val detailViewModelFactory = remember { PublicationDetailViewModelFactory(repository) }
    val backStack by navigator.navStack.collectAsStateWithLifecycle()

    AppTheme(darkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(Res.string.app_title)) },
                    navigationIcon = {
                        if (backStack.size > 1) {
                            TextButton(onClick = navigator::back) {
                                Text(stringResource(Res.string.back))
                            }
                        }
                    },
                    actions = {
                        TextButton(onClick = { darkTheme = !darkTheme }) {
                            Text(stringResource(Res.string.theme_toggle))
                        }
                    },
                )
            },
        ) { insets ->
            AppNavDisplay(
                navigator = navigator,
                listViewModelFactory = listViewModelFactory,
                detailViewModelFactory = detailViewModelFactory,
                modifier = Modifier.padding(insets),
            )
        }
    }
}
