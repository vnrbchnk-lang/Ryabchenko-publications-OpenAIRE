package ru.omgtu.ryabchenko.publications.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import publicationscmp.shared.generated.resources.Res
import publicationscmp.shared.generated.resources.authors_label
import publicationscmp.shared.generated.resources.nothing_found
import publicationscmp.shared.generated.resources.publication_date_label
import publicationscmp.shared.generated.resources.publisher_label
import publicationscmp.shared.generated.resources.related_label
import publicationscmp.shared.generated.resources.subjects_label
import publicationscmp.shared.generated.resources.type_label
import publicationscmp.shared.generated.resources.type_dataset
import publicationscmp.shared.generated.resources.type_other
import publicationscmp.shared.generated.resources.type_publication
import publicationscmp.shared.generated.resources.type_software
import ru.omgtu.ryabchenko.publications.domain.PublicationType
import ru.omgtu.ryabchenko.publications.ui.components.LoadErrorView
import ru.omgtu.ryabchenko.publications.ui.model.PublicationDetailUi

@Composable
fun PublicationDetailScreen(
    state: PublicationDetailState,
    onIntent: (PublicationDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        PublicationDetailState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        PublicationDetailState.NotFound -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(Res.string.nothing_found), style = MaterialTheme.typography.bodyLarge)
            }
        }

        is PublicationDetailState.Failed -> {
            LoadErrorView(
                error = state.error,
                onRetry = { onIntent(PublicationDetailIntent.Retry) },
                modifier = modifier,
            )
        }

        is PublicationDetailState.Loaded -> {
            PublicationDetailContent(
                details = state.details,
                onIntent = onIntent,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun PublicationDetailContent(
    details: PublicationDetailUi,
    onIntent: (PublicationDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(details.mainTitle, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        DetailField(stringResource(Res.string.authors_label), details.authors)
        DetailField(stringResource(Res.string.publication_date_label), details.publicationDate)
        DetailField(stringResource(Res.string.publisher_label), details.publisher)
        DetailField(stringResource(Res.string.type_label), stringResource(details.type.labelRes()))
        DetailField(stringResource(Res.string.subjects_label), details.subjects)
        Spacer(Modifier.height(16.dp))
        Text(details.description, style = MaterialTheme.typography.bodyLarge)
        if (details.related.isNotEmpty()) {
            Spacer(Modifier.height(24.dp))
            Text(stringResource(Res.string.related_label), style = MaterialTheme.typography.titleMedium)
            details.related.forEach { related ->
                Text(
                    text = related.mainTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onIntent(PublicationDetailIntent.RelatedClicked(related.id)) }
                        .padding(vertical = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun DetailField(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

private fun PublicationType.labelRes() = when (this) {
    PublicationType.Publication -> Res.string.type_publication
    PublicationType.Dataset -> Res.string.type_dataset
    PublicationType.Software -> Res.string.type_software
    PublicationType.Other -> Res.string.type_other
}
