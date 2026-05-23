package com.ulyup.tier_list.feature.error.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ulyup.tier_list.feature.error.ErrorScreen
import kotlinx.serialization.Serializable

@Serializable
data object ErrorGraph

@Serializable
data object ErrorRoute

fun NavGraphBuilder.errorGraph(onRetry: () -> Unit) {
    navigation<ErrorGraph>(startDestination = ErrorRoute) {
        composable<ErrorRoute> { ErrorScreen(onRetry = onRetry) }
    }
}