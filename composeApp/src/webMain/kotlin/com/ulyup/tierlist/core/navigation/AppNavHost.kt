package com.ulyup.tierlist.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ulyup.tierlist.feature.auth.navigation.AuthGraph
import com.ulyup.tierlist.feature.auth.navigation.authGraph
import com.ulyup.tierlist.feature.feed.navigation.feedGraph
import com.ulyup.tierlist.feature.mylists.navigation.myListsGraph

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AuthGraph,
        modifier = modifier,
    ) {
        authGraph()
        feedGraph()
        myListsGraph()
    }
}