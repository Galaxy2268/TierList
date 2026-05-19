package com.ulyup.tierlist.main

import androidx.compose.runtime.Composable
import com.ulyup.tierlist.core.navigation.AppNavHost
import com.ulyup.tierlist.main.vm.MainViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen() {
    val viewModel = koinViewModel<MainViewModel>()
    AppNavHost(startDestination = viewModel.uiState.startDestination)
}