package ru.omgtu.ryabchenko.publications.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import ru.omgtu.ryabchenko.publications.ui.detail.PublicationDetailScreen
import ru.omgtu.ryabchenko.publications.ui.detail.PublicationDetailViewModel
import ru.omgtu.ryabchenko.publications.ui.detail.PublicationDetailViewModelFactory
import ru.omgtu.ryabchenko.publications.ui.list.PublicationListScreen
import ru.omgtu.ryabchenko.publications.ui.list.PublicationListViewModel
import ru.omgtu.ryabchenko.publications.ui.list.PublicationListViewModelFactory

private const val TRANSITION_MS = 300

@Composable
fun AppNavDisplay(
    navigator: Navigator,
    listViewModelFactory: PublicationListViewModelFactory,
    detailViewModelFactory: PublicationDetailViewModelFactory,
    modifier: Modifier = Modifier,
) {
    val backStack by navigator.navStack.collectAsStateWithLifecycle()
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = navigator::back,
        entryProvider = entryProvider {
            entry<Route.List> {
                val viewModel: PublicationListViewModel = viewModel(factory = listViewModelFactory)
                val state by viewModel.state.collectAsStateWithLifecycle()
                PublicationListScreen(state = state, onIntent = viewModel::onIntent)
            }
            entry<Route.Detail> { route ->
                val viewModel: PublicationDetailViewModel = viewModel(
                    key = "detail-${route.id}",
                    factory = detailViewModelFactory,
                    extras = PublicationDetailViewModelFactory.extrasFor(route.id),
                )
                val state by viewModel.state.collectAsStateWithLifecycle()
                state?.let { PublicationDetailScreen(publication = it) }
            }
        },
        transitionSpec = { slide(SlideDirection.Start) },
        popTransitionSpec = { slide(SlideDirection.End) },
        predictivePopTransitionSpec = { slide(SlideDirection.End) },
    )
}

private fun AnimatedContentTransitionScope<Scene<Route>>.slide(
    direction: SlideDirection,
): ContentTransform =
    (slideIntoContainer(direction, tween(TRANSITION_MS)) + fadeIn(tween(TRANSITION_MS)))
        .togetherWith(
            slideOutOfContainer(direction, tween(TRANSITION_MS)) + fadeOut(tween(TRANSITION_MS)),
        )
