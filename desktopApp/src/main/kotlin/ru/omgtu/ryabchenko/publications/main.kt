package ru.omgtu.ryabchenko.publications

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.compose.resources.stringResource
import publicationscmp.shared.generated.resources.Res
import publicationscmp.shared.generated.resources.app_title

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_title),
    ) {
        App()
    }
}