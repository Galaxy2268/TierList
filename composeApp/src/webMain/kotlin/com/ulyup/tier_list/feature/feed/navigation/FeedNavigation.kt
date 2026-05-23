package com.ulyup.tier_list.feature.feed.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ulyup.tier_list.feature.feed.FeedScreen
import kotlinx.serialization.Serializable

@Serializable
data object FeedGraph

@Serializable
data object FeedRoute

fun NavGraphBuilder.feedGraph(onOpenTierList: (Int) -> Unit) {
    navigation<FeedGraph>(startDestination = FeedRoute) {
        composable<FeedRoute> { FeedScreen(onOpenTierList = onOpenTierList) }
    }
}
