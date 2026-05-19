package com.ulyup.tierlist.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ulyup.tierlist.feature.auth.navigation.authGraph
import com.ulyup.tierlist.feature.auth.navigation.navigateToRegister
import com.ulyup.tierlist.feature.feed.navigation.feedGraph
import com.ulyup.tierlist.feature.mylists.navigation.myListsGraph
import com.ulyup.tierlist.feature.splash.navigation.splashGraph

@Composable
fun AppNavHost(
    startDestination: Any,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        splashGraph()
        authGraph(
            onNavigateToRegister = { navController.navigateToRegister() },
            onBack = { navController.popBackStack() },
        )
        feedGraph()
        myListsGraph()
    }
}