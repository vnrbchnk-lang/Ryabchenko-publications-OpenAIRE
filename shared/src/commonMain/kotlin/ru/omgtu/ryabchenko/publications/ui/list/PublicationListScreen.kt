package ru.omgtu.ryabchenko.publications.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import publicationscmp.shared.generated.resources.Res
import publicationscmp.shared.generated.resources.empty_list
import publicationscmp.shared.generated.resources.nothing_found
import publicationscmp.shared.generated.resources.retry
import publicationscmp.shared.generated.resources.search_hint
import ru.omgtu.ryabchenko.publications.ui.components.LoadErrorView
import ru.omgtu.ryabchenko.publications.ui.model.PublicationCardUi

@Composable
fun PublicationListScreen(
    state: PublicationListState,
    onIntent: (PublicationListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize()) {
        OutlinedTextField(
            value = state.query,
            onValueChange = { onIntent(PublicationListIntent.QueryChanged(it)) },
            label = { Text(stringResource(Res.string.search_hint)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )
        when (state) {
            is PublicationListState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is PublicationListState.Failed -> {
                LoadErrorView(
                    error = state.error,
                    onRetry = { onIntent(PublicationListIntent.Retry) },
                    modifier = Modifier.fillMaxWidth().weight(1f),
                )
            }

            is PublicationListState.Loaded -> {
                if (state.items.isEmpty()) {
                    val text = if (state.query.isBlank()) Res.string.empty_list else Res.string.nothing_found
                    Box(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(stringResource(text), style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    PublicationList(
                        state = state,
                        onIntent = onIntent,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun PublicationList(
    state: PublicationListState.Loaded,
    onIntent: (PublicationListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val lastVisibleIndex by remember {
        derivedStateOf { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0 }
    }
    LaunchedEffect(lastVisibleIndex, state.items.size, state.isLoadingMore, state.loadMoreError) {
        if (state.hasMore && !state.isLoadingMore && state.loadMoreError == null &&
            lastVisibleIndex >= state.items.size - 3
        ) {
            onIntent(PublicationListIntent.LoadMore)
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(state.items, key = { it.id }) { publication ->
            PublicationCard(
                publication = publication,
                onClick = { onIntent(PublicationListIntent.CardClicked(publication.id)) },
            )
        }
        if (state.hasMore) {
            item {
                LoadMoreFooter(
                    isLoading = state.isLoadingMore,
                    hasError = state.loadMoreError != null,
                    onRetry = { onIntent(PublicationListIntent.LoadMore) },
                )
            }
        }
    }
}

@Composable
private fun LoadMoreFooter(
    isLoading: Boolean,
    hasError: Boolean,
    onRetry: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        when {
            isLoading -> CircularProgressIndicator()
            hasError -> TextButton(onClick = onRetry) { Text(stringResource(Res.string.retry)) }
        }
    }
}

@Composable
private fun PublicationCard(
    publication: PublicationCardUi,
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
            Text(publication.authors, style = MaterialTheme.typography.bodyMedium)
            Text(publication.publicationDate, style = MaterialTheme.typography.bodySmall)
        }
    }
}
