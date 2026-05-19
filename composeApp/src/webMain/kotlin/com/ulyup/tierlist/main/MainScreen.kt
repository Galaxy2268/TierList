package com.ulyup.tierlist.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.ulyup.tierlist.core.mvi.ObserveAsEvents
import com.ulyup.tierlist.core.navigation.AppNavHost
import com.ulyup.tierlist.core.ui.components.state.LoadingState
import com.ulyup.tierlist.feature.auth.navigation.navigateToAuth
import com.ulyup.tierlist.feature.feed.navigation.navigateToFeed
import com.ulyup.tierlist.main.vm.MainViewModel
import com.ulyup.tierlist.main.vm.NavigateToAuthEvent
import com.ulyup.tierlist.main.vm.NavigateToFeedEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen() {
    val viewModel = koinViewModel<MainViewModel>()
    val state = viewModel.uiState
    val navController = rememberNavController()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            NavigateToFeedEvent -> navController.navigateToFeed()
            NavigateToAuthEvent -> navController.navigateToAuth()
        }
    }

    if (!state.isReady) {
        LoadingState()
    } else {
        AppNavHost(navController = navController)
    }
}