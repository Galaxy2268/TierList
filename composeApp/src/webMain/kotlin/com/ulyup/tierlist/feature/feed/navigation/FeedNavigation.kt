package com.ulyup.tierlist.feature.feed.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ulyup.tierlist.feature.feed.FeedScreen
import kotlinx.serialization.Serializable

@Serializable
data object FeedGraph

@Serializable
data object FeedRoute

fun NavGraphBuilder.feedGraph() {
    navigation<FeedGraph>(startDestination = FeedRoute) {
        composable<FeedRoute> { FeedScreen() }
    }
}
