package com.ulyup.tier_list.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ulyup.tier_list.core.browser.ShareDetailLink
import com.ulyup.tier_list.core.ui.snackbar.LocalTierListSnackbarHandler
import com.ulyup.tier_list.core.ui.snackbar.TierListSnackbarHandler
import com.ulyup.tier_list.core.ui.snackbar.TierListSnackbarHost
import com.ulyup.tier_list.feature.auth.navigation.authGraph
import com.ulyup.tier_list.feature.auth.navigation.navigateToRegister
import com.ulyup.tier_list.feature.error.navigation.errorGraph
import com.ulyup.tier_list.feature.feed.navigation.FeedGraph
import com.ulyup.tier_list.feature.feed.navigation.feedGraph
import com.ulyup.tier_list.feature.mylists.navigation.MyListsGraph
import com.ulyup.tier_list.feature.mylists.navigation.myListsGraph
import com.ulyup.tier_list.feature.profile.navigation.ProfileGraph
import com.ulyup.tier_list.feature.profile.navigation.profileGraph
import com.ulyup.tier_list.feature.splash.navigation.splashGraph
import com.ulyup.tier_list.feature.tier_list_detail.navigation.TierListDetailRoute
import com.ulyup.tier_list.feature.tier_list_detail.navigation.navigateToTierListDetail
import com.ulyup.tier_list.feature.tier_list_detail.navigation.tierListDetailGraph
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.error_message
import com.ulyup.tier_list.theme.appColors
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppNavHost(
    startDestination: Any,
    initialDetailId: Int?,
    onRetryBootstrap: () -> Unit,
    onDetailEnter: (Int) -> Unit,
    onDetailExit: () -> Unit,
    topBar: @Composable (currentDestination: NavDestination?, onTabSelected: (Any) -> Unit) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    val genericErrorMessage = stringResource(Res.string.error_message)
    val snackbarHandler = remember(genericErrorMessage) {
        TierListSnackbarHandler(SnackbarHostState(), genericErrorMessage)
    }

    val openTierList: (Int) -> Unit = { id ->
        onDetailEnter(id)
        ShareDetailLink.setInUrl(id)
        navController.navigateToTierListDetail(id)
    }

    var initialDetailHandled by remember { mutableStateOf(false) }
    LaunchedEffect(currentDestination, initialDetailId) {
        if (initialDetailHandled) return@LaunchedEffect
        if (initialDetailId == null) return@LaunchedEffect
        if (currentDestination?.isOnTabGraph() != true) return@LaunchedEffect
        openTierList(initialDetailId)
        initialDetailHandled = true
    }

    CompositionLocalProvider(LocalTierListSnackbarHandler provides snackbarHandler) {
        Scaffold(
            modifier = modifier,
            containerColor = appColors.background,
            topBar = {
                topBar(currentDestination) { target ->
                    navController.navigate(target) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            snackbarHost = { TierListSnackbarHost(snackbarHandler) },
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
                feedGraph(onOpenTierList = openTierList)
                myListsGraph(onOpenTierList = openTierList)
                profileGraph()
                tierListDetailGraph(
                    onBack = {
                        if (navController.popBackStack<TierListDetailRoute>(inclusive = true)) {
                            onDetailExit()
                            ShareDetailLink.clearFromUrl()
                        }
                    },
                )
            }
        }
    }
}

private fun NavDestination.isOnTabGraph(): Boolean =
    hierarchy.any { destination ->
        destination.hasRoute(FeedGraph::class) ||
            destination.hasRoute(MyListsGraph::class) ||
            destination.hasRoute(ProfileGraph::class)
    }
