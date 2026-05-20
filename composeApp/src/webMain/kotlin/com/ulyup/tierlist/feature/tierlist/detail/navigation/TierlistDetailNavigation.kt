package com.ulyup.tierlist.feature.tierlist.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ulyup.tierlist.feature.tierlist.detail.TierlistDetailScreen
import kotlinx.serialization.Serializable

@Serializable
data class TierlistDetailRoute(val tierlistId: Int)

fun NavGraphBuilder.tierlistDetailGraph(onBack: () -> Unit) {
    composable<TierlistDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<TierlistDetailRoute>()
        TierlistDetailScreen(
            tierlistId = route.tierlistId,
            onBack = onBack,
        )
    }
}

fun NavController.navigateToTierlistDetail(tierlistId: Int) {
    navigate(TierlistDetailRoute(tierlistId))
}
