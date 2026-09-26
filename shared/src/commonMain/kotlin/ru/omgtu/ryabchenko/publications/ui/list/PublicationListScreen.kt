package ru.omgtu.ryabchenko.publications.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.omgtu.ryabchenko.publications.data.Publication

@Composable
fun PublicationListScreen(
    publications: List<Publication>,
    onPublicationClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(publications, key = { it.id }) { publication ->
            PublicationCard(
                publication = publication,
                onClick = { onPublicationClick(publication.id) },
            )
        }
    }
}

@Composable
private fun PublicationCard(
    publication: Publication,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(publication.mainTitle, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(publication.authors.joinToString(", "), style = MaterialTheme.typography.bodyMedium)
            Text(publication.publicationDate, style = MaterialTheme.typography.bodySmall)
        }
    }
}
