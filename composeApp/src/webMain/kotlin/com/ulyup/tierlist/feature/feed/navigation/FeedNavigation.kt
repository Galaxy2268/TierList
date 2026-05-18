package com.ulyup.tierlist.feature.feed.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ulyup.tierlist.feature.feed.FeedScreen
import kotlinx.serialization.Serializable

@Serializable
data object FeedGraph

@Serializable
data object FeedRoute

// Future: tier list detail viewed from public feed
// @Serializable data class ExternalTierListRoute(val tierlistId: Int)

fun NavGraphBuilder.feedGraph() {
    navigation<FeedGraph>(startDestination = FeedRoute) {
        composable<FeedRoute> { FeedScreen() }
        // composable<ExternalTierListRoute> { entry ->
        //     val args = entry.toRoute<ExternalTierListRoute>()
        //     ExternalTierListScreen(args.tierlistId)
        // }
    }
}

fun NavController.navigateToFeed() {
    navigate(FeedGraph) {
        popUpTo(graph.startDestinationId) { inclusive = true }
        launchSingleTop = true
    }
}
