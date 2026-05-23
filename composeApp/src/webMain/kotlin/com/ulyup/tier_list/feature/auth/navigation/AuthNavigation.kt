package com.ulyup.tier_list.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ulyup.tier_list.feature.auth.LoginScreen
import com.ulyup.tier_list.feature.auth.RegisterScreen
import kotlinx.serialization.Serializable

@Serializable
data object AuthGraph

@Serializable
data object LoginRoute

@Serializable
data object RegisterRoute

fun NavGraphBuilder.authGraph(
    onNavigateToRegister: () -> Unit,
    onBack: () -> Unit,
) {
    navigation<AuthGraph>(startDestination = LoginRoute) {
        composable<LoginRoute> {
            LoginScreen(onNavigateToRegister = onNavigateToRegister)
        }
        composable<RegisterRoute> {
            RegisterScreen(onBack = onBack)
        }
    }
}

fun NavController.navigateToAuth() {
    navigate(AuthGraph) {
        popUpTo(graph.startDestinationId) { inclusive = true }
        launchSingleTop = true
    }
}

fun NavController.navigateToRegister() {
    navigate(RegisterRoute) {
        launchSingleTop = true
    }
}