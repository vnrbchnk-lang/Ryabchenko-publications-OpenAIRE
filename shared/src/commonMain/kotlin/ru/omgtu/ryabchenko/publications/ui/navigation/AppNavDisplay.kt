package ru.omgtu.ryabchenko.publications.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import ru.omgtu.ryabchenko.publications.data.Publication
import ru.omgtu.ryabchenko.publications.ui.detail.PublicationDetailScreen
import ru.omgtu.ryabchenko.publications.ui.list.PublicationListScreen

private const val TRANSITION_MS = 300

@Composable
fun AppNavDisplay(
    backStack: MutableList<Route>,
    publications: List<Publication>,
    modifier: Modifier = Modifier,
) {
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Route.List> {
                PublicationListScreen(
                    publications = publications,
                    onPublicationClick = { id -> backStack.add(Route.Detail(id)) },
                )
            }
            entry<Route.Detail> { route ->
                val publication = publications.first { it.id == route.id }
                PublicationDetailScreen(publication = publication)
            }
        },
        transitionSpec = { slide(SlideDirection.Start) },
        popTransitionSpec = { slide(SlideDirection.End) },
    )
}

private fun AnimatedContentTransitionScope<Scene<Route>>.slide(
    direction: SlideDirection,
): ContentTransform =
    (slideIntoContainer(direction, tween(TRANSITION_MS)) + fadeIn(tween(TRANSITION_MS)))
        .togetherWith(
            slideOutOfContainer(direction, tween(TRANSITION_MS)) + fadeOut(tween(TRANSITION_MS)),
        )
