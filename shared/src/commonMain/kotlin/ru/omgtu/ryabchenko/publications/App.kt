package ru.omgtu.ryabchenko.publications

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import publicationscmp.shared.generated.resources.Res
import publicationscmp.shared.generated.resources.back
import publicationscmp.shared.generated.resources.list_title
import publicationscmp.shared.generated.resources.theme_toggle
import ru.omgtu.ryabchenko.publications.data.mockPublications
import ru.omgtu.ryabchenko.publications.ui.navigation.AppNavDisplay
import ru.omgtu.ryabchenko.publications.ui.navigation.Route
import ru.omgtu.ryabchenko.publications.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var darkTheme by remember { mutableStateOf(false) }
    val backStack = remember { mutableStateListOf<Route>(Route.List) }

    AppTheme(darkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(Res.string.list_title)) },
                    navigationIcon = {
                        if (backStack.size > 1) {
                            TextButton(onClick = { backStack.removeLastOrNull() }) {
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
                backStack = backStack,
                publications = mockPublications,
                modifier = Modifier.padding(insets),
            )
        }
    }
}
