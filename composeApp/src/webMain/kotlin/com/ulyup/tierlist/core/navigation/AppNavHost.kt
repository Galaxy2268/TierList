package com.ulyup.tierlist.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ulyup.tierlist.feature.auth.navigation.authGraph
import com.ulyup.tierlist.feature.auth.navigation.navigateToRegister
import com.ulyup.tierlist.feature.error.navigation.errorGraph
import com.ulyup.tierlist.feature.feed.navigation.feedGraph
import com.ulyup.tierlist.feature.mylists.navigation.myListsGraph
import com.ulyup.tierlist.feature.profile.navigation.profileGraph
import com.ulyup.tierlist.feature.splash.navigation.splashGraph
import com.ulyup.tierlist.feature.tierlist_detail.navigation.navigateToTierlistDetail
import com.ulyup.tierlist.feature.tierlist_detail.navigation.tierlistDetailGraph
import com.ulyup.tierlist.theme.appColors

@Composable
fun AppNavHost(
    startDestination: Any,
    onRetryBootstrap: () -> Unit,
    topBar: @Composable (currentDestination: NavDestination?, onTabSelected: (Any) -> Unit) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    Scaffold(
        modifier = modifier,
        containerColor = appColors.background,
        topBar = {
            topBar(currentDestination) { target ->
                navController.navigate(target) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(appColors.background),
        ) {
            splashGraph()
            errorGraph(onRetry = onRetryBootstrap)
            authGraph(
                onNavigateToRegister = navController::navigateToRegister,
                onBack = navController::popBackStack,
            )
            feedGraph(onOpenTierlist = navController::navigateToTierlistDetail)
            myListsGraph(onOpenTierlist = navController::navigateToTierlistDetail)
            profileGraph()
            tierlistDetailGraph(onBack = navController::popBackStack)
        }
    }
}
