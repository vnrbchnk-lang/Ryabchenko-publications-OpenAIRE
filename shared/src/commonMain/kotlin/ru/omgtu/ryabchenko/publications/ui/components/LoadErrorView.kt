package ru.omgtu.ryabchenko.publications.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import publicationscmp.shared.generated.resources.Res
import publicationscmp.shared.generated.resources.error_bad_response
import publicationscmp.shared.generated.resources.error_no_connection
import publicationscmp.shared.generated.resources.error_server
import publicationscmp.shared.generated.resources.error_timeout
import publicationscmp.shared.generated.resources.retry
import ru.omgtu.ryabchenko.publications.domain.LoadException

@Composable
fun LoadErrorView(
    error: LoadException,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(stringResource(error.messageRes()))
        Spacer(Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text(stringResource(Res.string.retry))
        }
    }
}

private fun LoadException.messageRes(): StringResource = when (this) {
    LoadException.NoConnection -> Res.string.error_no_connection
    LoadException.Timeout -> Res.string.error_timeout
    is LoadException.Server -> Res.string.error_server
    LoadException.NotFound,
    LoadException.BadResponse,
    -> Res.string.error_bad_response
}
