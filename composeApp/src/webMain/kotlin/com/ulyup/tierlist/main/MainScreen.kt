package com.ulyup.tierlist.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ulyup.tierlist.core.mvi.ObserveAsEvents
import com.ulyup.tierlist.core.navigation.AppNavHost
import com.ulyup.tierlist.core.navigation.AppNavigator
import com.ulyup.tierlist.core.navigation.LocalAppNavigator
import com.ulyup.tierlist.core.ui.components.LoadingState
import com.ulyup.tierlist.main.vm.MainEvent
import com.ulyup.tierlist.main.vm.MainViewModel
import com.ulyup.tierlist.theme.appColors
import com.ulyup.tierlist.theme.appTypography
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val viewModel = koinViewModel<MainViewModel>()
    val state = viewModel.uiState
    val navController = rememberNavController()
    val appNavigator = remember(navController) { AppNavigator(navController) }

    ObserveAsEvents(viewModel.effects) { event ->
        when (event) {
            MainEvent.NavigateToFeed -> appNavigator.toFeed()
            MainEvent.NavigateToAuth -> appNavigator.toAuth()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "TierRank",
                        style = appTypography.titleLarge,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appColors.surface,
                    titleContentColor = appColors.onSurface,
                ),
            )
        },
        containerColor = appColors.background,
    ) { padding ->
        if (!state.isReady) {
            LoadingState(modifier = Modifier.padding(padding))
        } else {
            CompositionLocalProvider(LocalAppNavigator provides appNavigator) {
                AppNavHost(
                    navController = navController,
                    modifier = Modifier.padding(padding),
                )
            }
        }
    }
}