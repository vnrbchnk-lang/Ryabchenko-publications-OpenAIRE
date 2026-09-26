package ru.omgtu.ryabchenko.publications.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import publicationscmp.shared.generated.resources.Res
import publicationscmp.shared.generated.resources.authors_label
import publicationscmp.shared.generated.resources.publication_date_label
import publicationscmp.shared.generated.resources.publisher_label
import publicationscmp.shared.generated.resources.subjects_label
import publicationscmp.shared.generated.resources.type_label
import ru.omgtu.ryabchenko.publications.data.Publication

@Composable
fun PublicationDetailScreen(
    publication: Publication,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(publication.mainTitle, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        DetailField(stringResource(Res.string.authors_label), publication.authors.joinToString(", "))
        DetailField(stringResource(Res.string.publication_date_label), publication.publicationDate)
        DetailField(stringResource(Res.string.publisher_label), publication.publisher)
        DetailField(stringResource(Res.string.type_label), publication.type)
        DetailField(stringResource(Res.string.subjects_label), publication.subjects.joinToString(", "))
        Spacer(Modifier.height(16.dp))
        Text(publication.description, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun DetailField(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
