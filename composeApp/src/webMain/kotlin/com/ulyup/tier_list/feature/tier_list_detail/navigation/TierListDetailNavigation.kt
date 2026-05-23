package com.ulyup.tier_list.feature.tier_list_detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ulyup.tier_list.feature.tier_list_detail.TierListDetailScreen
import kotlinx.serialization.Serializable

@Serializable
data class TierListDetailRoute(val tierListId: Int)

fun NavGraphBuilder.tierListDetailGraph(onBack: () -> Unit) {
    composable<TierListDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<TierListDetailRoute>()
        TierListDetailScreen(
            tierListId = route.tierListId,
            onBack = onBack,
        )
    }
}

fun NavController.navigateToTierListDetail(tierListId: Int) {
    navigate(TierListDetailRoute(tierListId))
}
